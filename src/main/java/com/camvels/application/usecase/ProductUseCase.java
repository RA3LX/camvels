package com.camvels.application.usecase;

import com.camvels.application.port.in.ProductPort;
import com.camvels.domain.model.Movimiento;
import com.camvels.domain.model.Producto;
import com.camvels.domain.model.Usuario;
import com.camvels.domain.port.out.MovimientoRepository;
import com.camvels.domain.port.out.ProductoRepository;
import com.camvels.domain.port.out.ProveedorRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductUseCase implements ProductPort {

    private final ProductoRepository productoRepository;
    private final MovimientoRepository movimientoRepository;
    private final ProveedorRepository proveedorRepository;

    public ProductUseCase(ProductoRepository productoRepository,
                          MovimientoRepository movimientoRepository,
                          ProveedorRepository proveedorRepository) {
        this.productoRepository = productoRepository;
        this.movimientoRepository = movimientoRepository;
        this.proveedorRepository = proveedorRepository;
    }

    public List<Producto> listar(String categoria, String estado, String busqueda) {
        if (busqueda != null && !busqueda.isEmpty()) {
            List<Producto> lista = productoRepository.buscarPorTexto(busqueda);
            return aplicarFiltros(lista, categoria, estado);
        }
        if (categoria != null && !categoria.isEmpty()) {
            if (estado != null && !estado.isEmpty()) {
                return filtrarPorEstadoEspecial(productoRepository.listarPorCategoria(categoria), estado);
            }
            return productoRepository.listarPorCategoria(categoria);
        }
        if (estado != null && !estado.isEmpty()) {
            return filtrarPorEstadoGlobal(estado);
        }
        return productoRepository.listar();
    }

    public Optional<Producto> buscarPorId(int id) {
        return productoRepository.buscarPorId(id);
    }

    public List<com.camvels.domain.model.Proveedor> listarProveedores() {
        return proveedorRepository.listar();
    }

    public boolean guardar(Producto producto, Usuario usuario) {
        producto.actualizarStockTotal();
        if (producto.getId() == 0) {
            boolean creado = productoRepository.agregar(producto);
            if (creado && producto.getId() > 0 && producto.getStock() > 0 && usuario != null) {
                Movimiento movimientoInicial = new Movimiento();
                movimientoInicial.setTipo("ENTRADA");
                movimientoInicial.setProductoId(producto.getId());
                movimientoInicial.setCantidad(producto.getStock());
                movimientoInicial.setUsuarioId(usuario.getId());
                movimientoInicial.setObservaciones("Stock inicial al crear el producto");
                movimientoRepository.agregar(movimientoInicial);
            }
            return creado;
        }
        return productoRepository.actualizar(producto);
    }

    public boolean eliminar(int id) {
        return productoRepository.eliminar(id);
    }

    public List<Producto> listarStockBajo() {
        return productoRepository.listarStockBajo();
    }

    private List<Producto> aplicarFiltros(List<Producto> lista, String categoria, String estado) {
        List<Producto> resultado = lista;
        if (categoria != null && !categoria.isEmpty()) {
            resultado = resultado.stream()
                    .filter(p -> categoria.equals(p.getCategoria()))
                    .collect(Collectors.toList());
        }
        if (estado != null && !estado.isEmpty()) {
            resultado = filtrarPorEstadoEspecial(resultado, estado);
        }
        return resultado;
    }

    private List<Producto> filtrarPorEstadoGlobal(String estado) {
        return switch (estado) {
            case "con_ajustes" -> productoRepository.listarConAjustes();
            case "completamente_atendidos" -> productoRepository.listarCompletamenteAtendidos();
            case "pendientes_atencion" -> productoRepository.listarPendientesAtencion();
            default -> productoRepository.listarPorEstado(estado);
        };
    }

    private List<Producto> filtrarPorEstadoEspecial(List<Producto> base, String estado) {
        return switch (estado) {
            case "con_ajustes" -> intersectar(base, productoRepository.listarConAjustes());
            case "completamente_atendidos" -> intersectar(base, productoRepository.listarCompletamenteAtendidos());
            case "pendientes_atencion" -> intersectar(base, productoRepository.listarPendientesAtencion());
            default -> base.stream().filter(p -> estado.equals(p.getEstado())).collect(Collectors.toList());
        };
    }

    private List<Producto> intersectar(List<Producto> base, List<Producto> filtro) {
        Set<Integer> ids = new HashSet<>();
        for (Producto p : filtro) {
            ids.add(p.getId());
        }
        return base.stream().filter(p -> ids.contains(p.getId())).collect(Collectors.toList());
    }
}
