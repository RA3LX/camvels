package com.camvels.application.usecase;

import com.camvels.application.port.in.RegisterMovementPort;
import com.camvels.domain.exception.ProductoNoEncontradoException;
import com.camvels.domain.model.Movimiento;
import com.camvels.domain.model.Producto;
import com.camvels.domain.model.TipoMovimiento;
import com.camvels.domain.port.out.MovimientoRepository;
import com.camvels.domain.port.out.ProductoRepository;
import com.camvels.domain.port.out.TransactionPort;

public class RegisterMovementUseCase implements RegisterMovementPort {

    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final TransactionPort transactionPort;

    public RegisterMovementUseCase(MovimientoRepository movimientoRepository,
                                   ProductoRepository productoRepository,
                                   TransactionPort transactionPort) {
        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
        this.transactionPort = transactionPort;
    }

    public void ejecutar(TipoMovimiento tipo, int productoId, int cantidad, int usuarioId, String observaciones) {
        transactionPort.executeInTransaction(() -> {
            Producto producto = productoRepository.buscarPorId(productoId)
                    .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado"));

            producto.aplicarMovimiento(tipo, cantidad);

            Movimiento movimiento = new Movimiento();
            movimiento.setTipo(tipo.name());
            movimiento.setProductoId(productoId);
            movimiento.setCantidad(cantidad);
            movimiento.setUsuarioId(usuarioId);
            movimiento.setObservaciones(observaciones);

            if (!movimientoRepository.agregar(movimiento)) {
                throw new RuntimeException("Error al registrar el movimiento");
            }
            if (!productoRepository.actualizar(producto)) {
                throw new RuntimeException("Error al actualizar el stock del producto");
            }
        });
    }
}
