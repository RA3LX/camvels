package com.camvels.infrastructure.persistence.jdbc;

import com.camvels.domain.model.Producto;
import com.camvels.domain.port.out.ProductoRepository;
import com.camvels.infrastructure.persistence.JdbcConnectionProvider;
import java.sql.*;
import java.util.*;

public class JdbcProductoRepository implements ProductoRepository {

    private static final String STOCK_COLUMNS =
            "COALESCE(stock_buen_estado, CASE WHEN estado = 'buen_estado' THEN stock ELSE 0 END) as stock_buen_estado, "
                    + "COALESCE(stock_mal_estado, CASE WHEN estado = 'mal_estado' THEN stock ELSE 0 END) as stock_mal_estado ";

    private final JdbcConnectionProvider connectionProvider;

    public JdbcProductoRepository(JdbcConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @Override
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT *, " + STOCK_COLUMNS + "FROM productos";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(crearProductoDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Producto crearProductoDesdeResultSet(ResultSet rs) throws SQLException {
        Producto p = new Producto();
        p.setId(rs.getInt("id"));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setCategoria(rs.getString("categoria"));
        p.setStock(rs.getInt("stock"));
        try {
            p.setStockBuenEstado(rs.getInt("stock_buen_estado"));
        } catch (SQLException e) {
            int stockTotal = rs.getInt("stock");
            String estado = rs.getString("estado");
            if ("mal_estado".equals(estado)) {
                p.setStockBuenEstado(0);
                p.setStockMalEstado(stockTotal);
            } else {
                p.setStockBuenEstado(stockTotal);
                p.setStockMalEstado(0);
            }
        }
        try {
            p.setStockMalEstado(rs.getInt("stock_mal_estado"));
        } catch (SQLException ignored) {
        }
        p.setMinimo(rs.getInt("minimo"));
        p.setPrecio(rs.getDouble("precio"));
        p.setEstado(rs.getString("estado"));
        try {
            int proveedorId = rs.getInt("proveedor_id");
            if (!rs.wasNull()) {
                p.setProveedorId(proveedorId);
            }
        } catch (SQLException e) {
            p.setProveedorId(null);
        }
        return p;
    }

    @Override
    public boolean agregar(Producto p) {
        p.actualizarStockTotal();
        String sql = "INSERT INTO productos (codigo, nombre, categoria, stock, stock_buen_estado, stock_mal_estado, minimo, precio, estado, proveedor_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getStockBuenEstado());
            ps.setInt(6, p.getStockMalEstado());
            ps.setInt(7, p.getMinimo());
            ps.setDouble(8, p.getPrecio());
            ps.setString(9, p.getEstado());
            if (p.getProveedorId() != null) {
                ps.setInt(10, p.getProveedorId());
            } else {
                ps.setNull(10, Types.INTEGER);
            }
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && (e.getMessage().contains("stock_buen_estado")
                    || e.getMessage().contains("stock_mal_estado")
                    || e.getMessage().contains("proveedor_id"))) {
                return agregarSinStockSeparado(p);
            }
            e.printStackTrace();
        }
        return false;
    }

    private boolean agregarSinStockSeparado(Producto p) {
        String sql = "INSERT INTO productos (codigo, nombre, categoria, stock, minimo, precio, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getMinimo());
            ps.setDouble(6, p.getPrecio());
            ps.setString(7, p.getEstado());
            if (ps.executeUpdate() > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        p.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean actualizar(Producto p) {
        p.actualizarStockTotal();
        String sql = "UPDATE productos SET codigo=?, nombre=?, categoria=?, stock=?, stock_buen_estado=?, stock_mal_estado=?, minimo=?, precio=?, estado=?, proveedor_id=? WHERE id=?";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getStockBuenEstado());
            ps.setInt(6, p.getStockMalEstado());
            ps.setInt(7, p.getMinimo());
            ps.setDouble(8, p.getPrecio());
            ps.setString(9, p.getEstado());
            if (p.getProveedorId() != null) {
                ps.setInt(10, p.getProveedorId());
            } else {
                ps.setNull(10, Types.INTEGER);
            }
            ps.setInt(11, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            if (e.getMessage() != null && (e.getMessage().contains("stock_buen_estado")
                    || e.getMessage().contains("stock_mal_estado")
                    || e.getMessage().contains("proveedor_id"))) {
                return actualizarSinStockSeparado(p);
            }
            e.printStackTrace();
        }
        return false;
    }

    private boolean actualizarSinStockSeparado(Producto p) {
        String sql = "UPDATE productos SET codigo=?, nombre=?, categoria=?, stock=?, minimo=?, precio=?, estado=? WHERE id=?";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getCodigo());
            ps.setString(2, p.getNombre());
            ps.setString(3, p.getCategoria());
            ps.setInt(4, p.getStock());
            ps.setInt(5, p.getMinimo());
            ps.setDouble(6, p.getPrecio());
            ps.setString(7, p.getEstado());
            ps.setInt(8, p.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id=?";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<Producto> buscarPorId(int id) {
        String sql = "SELECT *, " + STOCK_COLUMNS + "FROM productos WHERE id=?";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Optional.of(crearProductoDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Producto> listarPorCategoria(String categoria) {
        return listarConFiltro("FROM productos WHERE categoria = ?", ps -> ps.setString(1, categoria));
    }

    @Override
    public List<Producto> listarPorEstado(String estado) {
        return listarConFiltro("FROM productos WHERE estado = ?", ps -> ps.setString(1, estado));
    }

    @Override
    public List<Producto> listarPorCategoriaYEstado(String categoria, String estado) {
        return listarConFiltro("FROM productos WHERE categoria = ? AND estado = ?", ps -> {
            ps.setString(1, categoria);
            ps.setString(2, estado);
        });
    }

    @Override
    public List<Producto> listarStockBajo() {
        return listarConFiltro("FROM productos WHERE stock <= minimo", null);
    }

    @Override
    public List<Producto> buscarPorTexto(String busqueda) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT *, " + STOCK_COLUMNS + "FROM productos WHERE codigo LIKE ? OR nombre LIKE ?";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            String patron = "%" + busqueda + "%";
            ps.setString(1, patron);
            ps.setString(2, patron);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(crearProductoDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Map<Integer, List<Object[]>> obtenerProductosConProblemasPorProveedor() {
        Map<Integer, List<Object[]>> productosPorProveedor = new LinkedHashMap<>();
        String sql = "SELECT p.id as producto_id, p.codigo, p.nombre, p.categoria, p.stock, p.minimo, "
                + "COALESCE(p.stock_buen_estado, CASE WHEN p.estado = 'buen_estado' THEN p.stock ELSE 0 END) as stock_buen_estado, "
                + "COALESCE(p.stock_mal_estado, CASE WHEN p.estado = 'mal_estado' THEN p.stock ELSE 0 END) as stock_mal_estado, "
                + "p.precio, p.estado, p.proveedor_id, "
                + "pr.nombre as proveedor_nombre, pr.ruc as proveedor_ruc, "
                + "pr.telefono as proveedor_telefono, pr.email as proveedor_email, "
                + "pr.direccion as proveedor_direccion "
                + "FROM productos p "
                + "LEFT JOIN proveedores pr ON p.proveedor_id = pr.id "
                + "WHERE (p.stock <= p.minimo OR COALESCE(p.stock_mal_estado, 0) > 0) "
                + "AND p.proveedor_id IS NOT NULL "
                + "ORDER BY pr.nombre, p.categoria, p.nombre";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int proveedorId = rs.getInt("proveedor_id");
                if (proveedorId > 0) {
                    Object[] productoInfo = new Object[16];
                    productoInfo[0] = rs.getInt("producto_id");
                    productoInfo[1] = rs.getString("codigo");
                    productoInfo[2] = rs.getString("nombre");
                    productoInfo[3] = rs.getString("categoria");
                    productoInfo[4] = rs.getInt("stock");
                    productoInfo[5] = rs.getInt("minimo");
                    productoInfo[6] = rs.getInt("stock_buen_estado");
                    productoInfo[7] = rs.getInt("stock_mal_estado");
                    productoInfo[8] = rs.getDouble("precio");
                    productoInfo[9] = rs.getString("estado");
                    productoInfo[10] = proveedorId;
                    productoInfo[11] = rs.getString("proveedor_nombre");
                    productoInfo[12] = rs.getString("proveedor_ruc");
                    productoInfo[13] = rs.getString("proveedor_telefono");
                    productoInfo[14] = rs.getString("proveedor_email");
                    productoInfo[15] = rs.getString("proveedor_direccion");
                    productosPorProveedor.computeIfAbsent(proveedorId, k -> new ArrayList<>()).add(productoInfo);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productosPorProveedor;
    }

    @Override
    public List<Producto> listarConAjustes() {
        return listarConJoin("INNER JOIN movimientos m ON p.id = m.producto_id WHERE m.tipo = 'AJUSTE'");
    }

    @Override
    public List<Producto> listarPendientesAtencion() {
        return listarConJoin("LEFT JOIN movimientos m ON p.id = m.producto_id AND m.tipo = 'AJUSTE' "
                + "WHERE (p.estado = 'mal_estado' OR COALESCE(p.stock_mal_estado, 0) > 0) AND m.id IS NULL");
    }

    @Override
    public List<Producto> listarCompletamenteAtendidos() {
        return listarConJoin("INNER JOIN movimientos m ON p.id = m.producto_id "
                + "WHERE p.estado = 'buen_estado' AND m.tipo = 'AJUSTE'");
    }

    @Override
    public Map<String, Integer> obtenerProductosPorCategoria() {
        return consultarConteoPorCampo("SELECT categoria, COUNT(*) as total FROM productos GROUP BY categoria ORDER BY total DESC");
    }

    @Override
    public Map<String, Integer> obtenerStockPorCategoria() {
        return consultarConteoPorCampo("SELECT categoria, SUM(stock) as total_stock FROM productos GROUP BY categoria ORDER BY total_stock DESC", "total_stock");
    }

    @Override
    public List<Object[]> obtenerTopProductosPorStock(int limite) {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT codigo, nombre, stock, categoria FROM productos ORDER BY stock DESC LIMIT ?";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limite);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Object[]{
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getInt("stock"),
                        rs.getString("categoria")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public Map<String, Integer> obtenerEstadisticasStock() {
        Map<String, Integer> mapa = new HashMap<>();
        String sql = "SELECT "
                + "SUM(COALESCE(stock_buen_estado, CASE WHEN estado = 'buen_estado' THEN stock ELSE 0 END)) as stock_bueno, "
                + "SUM(COALESCE(stock_mal_estado, CASE WHEN estado = 'mal_estado' THEN stock ELSE 0 END)) as stock_malo "
                + "FROM productos";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                mapa.put("buen_estado", rs.getInt("stock_bueno"));
                mapa.put("mal_estado", rs.getInt("stock_malo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapa;
    }

    @FunctionalInterface
    private interface SqlBinder {
        void bind(PreparedStatement ps) throws SQLException;
    }

    private List<Producto> listarConFiltro(String fromWhere, SqlBinder binder) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT *, " + STOCK_COLUMNS + fromWhere;
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (binder != null) {
                binder.bind(ps);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(crearProductoDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private List<Producto> listarConJoin(String joinClause) {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT p.*, "
                + "COALESCE(p.stock_buen_estado, CASE WHEN p.estado = 'buen_estado' THEN p.stock ELSE 0 END) as stock_buen_estado, "
                + "COALESCE(p.stock_mal_estado, CASE WHEN p.estado = 'mal_estado' THEN p.stock ELSE 0 END) as stock_mal_estado "
                + "FROM productos p " + joinClause + " ORDER BY p.categoria, p.nombre";
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(crearProductoDesdeResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Map<String, Integer> consultarConteoPorCampo(String sql) {
        return consultarConteoPorCampo(sql, "total");
    }

    private Map<String, Integer> consultarConteoPorCampo(String sql, String totalColumn) {
        Map<String, Integer> mapa = new LinkedHashMap<>();
        try (Connection con = connectionProvider.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                mapa.put(rs.getString("categoria"), rs.getInt(totalColumn));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapa;
    }
}
