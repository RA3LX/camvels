package com.camvels.application.port.in;

import com.camvels.domain.model.TipoMovimiento;

public interface RegisterMovementPort {

    void ejecutar(TipoMovimiento tipo, int productoId, int cantidad, int usuarioId, String observaciones);
}
