package com.camvels.infrastructure.adapter.in.web;

import com.camvels.application.dto.ReportRequest;
import com.camvels.application.port.in.GenerateReportPort;
import com.camvels.domain.model.Proveedor;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR','ALMACEN')")
public class ReportController {

    private final GenerateReportPort generateReportPort;

    public ReportController(GenerateReportPort generateReportPort) {
        this.generateReportPort = generateReportPort;
    }

    @GetMapping("/suppliers")
    public List<Proveedor> suppliers() {
        return generateReportPort.listarProveedoresParaFormulario();
    }

    @GetMapping("/pdf")
    public void download(
            @RequestParam String tipo,
            @RequestParam(required = false) Integer proveedorId,
            @RequestParam(required = false, defaultValue = "false") boolean incluirStockBajo,
            @RequestParam(required = false, defaultValue = "false") boolean incluirMalEstado,
            HttpServletResponse response) throws IOException {
        ReportRequest request = new ReportRequest(tipo);
        request.setProveedorId(proveedorId);
        request.setIncluirStockBajo(incluirStockBajo);
        request.setIncluirMalEstado(incluirMalEstado);

        if (request.requiereFiltroProductos() && !request.tieneFiltroProductosValido()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe seleccionar al menos un tipo de producto.");
        }

        String filename = generateReportPort.construirNombreArchivo(request) + ".pdf";
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);
        generateReportPort.generar(request, response.getOutputStream());
    }
}
