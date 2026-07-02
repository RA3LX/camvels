package com.camvels.application.port.in;

import com.camvels.domain.model.Producto;
import java.util.List;
import java.util.Map;

public interface DashboardPort {

    List<Producto> listarProductos();

    List<Producto> listarStockBajo();

    int calcularTotalStock(List<Producto> productos);

    Map<String, Object> obtenerEstadisticas();
}
