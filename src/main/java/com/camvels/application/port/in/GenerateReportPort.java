package com.camvels.application.port.in;

import com.camvels.application.dto.ReportRequest;
import com.camvels.domain.model.Proveedor;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface GenerateReportPort {

    List<Proveedor> listarProveedoresParaFormulario();

    String construirNombreArchivo(ReportRequest request);

    void generar(ReportRequest request, OutputStream output) throws IOException;
}
