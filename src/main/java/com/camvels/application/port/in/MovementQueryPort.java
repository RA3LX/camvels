package com.camvels.application.port.in;

import com.camvels.domain.model.Movimiento;
import com.camvels.domain.model.Producto;
import java.util.List;

public interface MovementQueryPort {

    List<Movimiento> listar(String filtroTipo, String filtroProductoId);

    List<Producto> listarProductosParaFormulario();
}
