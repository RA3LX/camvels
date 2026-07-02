package com.camvels.domain.port.out;

import com.camvels.domain.model.Producto;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProductoRepository {
    List<Producto> listar();
    Optional<Producto> buscarPorId(int id);
    boolean agregar(Producto producto);
    boolean actualizar(Producto producto);
    boolean eliminar(int id);
    List<Producto> listarPorCategoria(String categoria);
    List<Producto> listarPorEstado(String estado);
    List<Producto> listarPorCategoriaYEstado(String categoria, String estado);
    List<Producto> listarStockBajo();
    List<Producto> buscarPorTexto(String busqueda);
    List<Producto> listarConAjustes();
    List<Producto> listarPendientesAtencion();
    List<Producto> listarCompletamenteAtendidos();
    Map<Integer, List<Object[]>> obtenerProductosConProblemasPorProveedor();
    Map<String, Integer> obtenerProductosPorCategoria();
    Map<String, Integer> obtenerStockPorCategoria();
    List<Object[]> obtenerTopProductosPorStock(int limite);
    Map<String, Integer> obtenerEstadisticasStock();
}
