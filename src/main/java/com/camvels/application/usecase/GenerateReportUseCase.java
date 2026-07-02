package com.camvels.application.usecase;

import com.camvels.application.dto.ReportRequest;
import com.camvels.application.port.in.GenerateReportPort;
import com.camvels.domain.model.Proveedor;
import com.camvels.domain.port.out.MovimientoRepository;
import com.camvels.domain.port.out.ProductoRepository;
import com.camvels.domain.port.out.ProveedorRepository;
import com.camvels.domain.port.out.ReportGeneratorPort;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GenerateReportUseCase implements GenerateReportPort {

    private final ProductoRepository productoRepository;
    private final MovimientoRepository movimientoRepository;
    private final ProveedorRepository proveedorRepository;
    private final ReportGeneratorPort reportGeneratorPort;

    public GenerateReportUseCase(ProductoRepository productoRepository,
                                 MovimientoRepository movimientoRepository,
                                 ProveedorRepository proveedorRepository,
                                 ReportGeneratorPort reportGeneratorPort) {
        this.productoRepository = productoRepository;
        this.movimientoRepository = movimientoRepository;
        this.proveedorRepository = proveedorRepository;
        this.reportGeneratorPort = reportGeneratorPort;
    }

    @Override
    public List<Proveedor> listarProveedoresParaFormulario() {
        return proveedorRepository.listar();
    }

    @Override
    public String construirNombreArchivo(ReportRequest request) {
        String nombreArchivo = "reporte_" + request.getTipo();
        if ("proveedores_productos".equals(request.getTipo())) {
            if (request.getProveedorId() != null && request.getProveedorId() > 0) {
                nombreArchivo += "_proveedor_" + request.getProveedorId();
            } else {
                nombreArchivo += "_todos";
            }

            if (request.isIncluirStockBajo() && request.isIncluirMalEstado()) {
                nombreArchivo += "_stock_mal";
            } else if (request.isIncluirStockBajo()) {
                nombreArchivo += "_stock";
            } else {
                nombreArchivo += "_mal";
            }
        }
        return nombreArchivo;
    }

    @Override
    public void generar(ReportRequest request, OutputStream output) throws IOException {
        switch (request.getTipo()) {
            case "productos" -> reportGeneratorPort.generarProductos(productoRepository.listar(), output);
            case "stock_bajo" -> reportGeneratorPort.generarStockBajo(productoRepository.listarStockBajo(), output);
            case "movimientos" -> reportGeneratorPort.generarMovimientos(movimientoRepository.listar(), output);
            case "proveedores" -> reportGeneratorPort.generarProveedores(proveedorRepository.listar(), output);
            case "proveedores_productos" -> reportGeneratorPort.generarProveedoresProductos(
                    request.isIncluirStockBajo(),
                    request.isIncluirMalEstado(),
                    filtrarProductosPorProveedor(request),
                    output);
            default -> throw new IllegalArgumentException("Tipo de reporte no válido: " + request.getTipo());
        }
    }

    private Map<Integer, List<Object[]>> filtrarProductosPorProveedor(ReportRequest request) {
        Map<Integer, List<Object[]>> productosPorProveedor =
                productoRepository.obtenerProductosConProblemasPorProveedor();
        Map<Integer, List<Object[]>> productosFiltrados = new LinkedHashMap<>();
        Integer proveedorIdFiltro = request.getProveedorId();

        for (Map.Entry<Integer, List<Object[]>> entry : productosPorProveedor.entrySet()) {
            Integer proveedorId = entry.getKey();
            List<Object[]> productos = entry.getValue();
            List<Object[]> productosValidos = new ArrayList<>();

            if (proveedorIdFiltro != null && proveedorIdFiltro > 0 && !proveedorIdFiltro.equals(proveedorId)) {
                continue;
            }

            for (Object[] producto : productos) {
                if (producto == null || producto.length < 10) {
                    continue;
                }

                int stock = producto[4] != null ? (Integer) producto[4] : 0;
                int minimo = producto[5] != null ? (Integer) producto[5] : 0;
                int stockMalEstado = producto[7] != null ? (Integer) producto[7] : 0;
                String estado = producto[9] != null ? (String) producto[9] : "";

                boolean tieneStockBajo = stock <= minimo;
                boolean estaMalEstado = stockMalEstado > 0 || "mal_estado".equals(estado);

                if ((request.isIncluirStockBajo() && tieneStockBajo)
                        || (request.isIncluirMalEstado() && estaMalEstado)) {
                    productosValidos.add(producto);
                }
            }

            if (!productosValidos.isEmpty()) {
                productosFiltrados.put(proveedorId, productosValidos);
            }
        }

        return productosFiltrados;
    }
}
