package com.camvels.infrastructure.adapter.in.web;

import com.camvels.application.port.in.DashboardPort;
import com.camvels.domain.model.Producto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardPort dashboardPort;

    public DashboardController(DashboardPort dashboardPort) {
        this.dashboardPort = dashboardPort;
    }

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        List<Producto> productos = dashboardPort.listarProductos();
        Map<String, Object> result = new HashMap<>();
        result.put("totalStock", dashboardPort.calcularTotalStock(productos));
        result.put("stockBajo", dashboardPort.listarStockBajo());
        return result;
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return dashboardPort.obtenerEstadisticas();
    }
}
