package com.camvels.infrastructure.adapter.in.web;

import com.camvels.application.port.in.MovementQueryPort;
import com.camvels.application.port.in.RegisterMovementPort;
import com.camvels.domain.model.Movimiento;
import com.camvels.domain.model.Producto;
import com.camvels.domain.model.TipoMovimiento;
import com.camvels.infrastructure.adapter.in.web.dto.MovementRequest;
import com.camvels.infrastructure.security.CurrentUser;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movements")
@PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
public class MovementController {

    private final MovementQueryPort movementQueryPort;
    private final RegisterMovementPort registerMovementPort;

    public MovementController(MovementQueryPort movementQueryPort, RegisterMovementPort registerMovementPort) {
        this.movementQueryPort = movementQueryPort;
        this.registerMovementPort = registerMovementPort;
    }

    @GetMapping
    public List<Movimiento> list(
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String producto) {
        return movementQueryPort.listar(tipo, producto);
    }

    @GetMapping("/products")
    public List<Producto> products() {
        return movementQueryPort.listarProductosParaFormulario();
    }

    @PostMapping
    public Map<String, String> create(@Valid @RequestBody MovementRequest request) {
        TipoMovimiento tipo = TipoMovimiento.fromString(request.tipo());
        int productoId = request.productoId();
        int cantidad = request.cantidad();
        String observaciones = request.observaciones() != null ? request.observaciones() : "";
        registerMovementPort.ejecutar(tipo, productoId, cantidad, CurrentUser.id(), observaciones);
        return Map.of("message", "Movimiento creado exitosamente");
    }
}
