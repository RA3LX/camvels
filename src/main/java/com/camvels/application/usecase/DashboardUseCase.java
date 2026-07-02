package com.camvels.application.usecase;

import com.camvels.application.port.in.DashboardPort;
import com.camvels.domain.model.Producto;
import com.camvels.domain.port.out.MovimientoRepository;
import com.camvels.domain.port.out.ProductoRepository;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DashboardUseCase implements DashboardPort {

    private final ProductoRepository productoRepository;
    private final MovimientoRepository movimientoRepository;

    public DashboardUseCase(ProductoRepository productoRepository,
                            MovimientoRepository movimientoRepository) {
        this.productoRepository = productoRepository;
        this.movimientoRepository = movimientoRepository;
    }

    public List<Producto> listarProductos() {
        return productoRepository.listar();
    }

    public List<Producto> listarStockBajo() {
        return productoRepository.listarStockBajo();
    }

    public int calcularTotalStock(List<Producto> productos) {
        return productos.stream().mapToInt(Producto::getStock).sum();
    }

    public Map<String, Object> obtenerEstadisticas() {
        Map<String, Object> estadisticas = new LinkedHashMap<>();
        estadisticas.put("productosPorCategoria", productoRepository.obtenerProductosPorCategoria());
        estadisticas.put("stockPorCategoria", productoRepository.obtenerStockPorCategoria());
        estadisticas.put("movimientosPorTipo", movimientoRepository.obtenerMovimientosPorTipo());
        estadisticas.put("cantidadPorTipo", movimientoRepository.obtenerCantidadPorTipo());
        estadisticas.put("movimientosPorFecha", movimientoRepository.obtenerMovimientosPorFecha(30));
        estadisticas.put("topProductos", productoRepository.obtenerTopProductosPorStock(10));
        estadisticas.put("estadisticasStock", productoRepository.obtenerEstadisticasStock());
        return estadisticas;
    }
}
