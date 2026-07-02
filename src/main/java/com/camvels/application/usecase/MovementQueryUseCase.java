package com.camvels.application.usecase;

import com.camvels.application.port.in.MovementQueryPort;
import com.camvels.domain.model.Movimiento;
import com.camvels.domain.model.Producto;
import com.camvels.domain.port.out.MovimientoRepository;
import com.camvels.domain.port.out.ProductoRepository;
import java.util.List;

public class MovementQueryUseCase implements MovementQueryPort {

    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;

    public MovementQueryUseCase(MovimientoRepository movimientoRepository,
                                ProductoRepository productoRepository) {
        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
    }

    public List<Movimiento> listar(String filtroTipo, String filtroProductoId) {
        if (filtroTipo != null && !filtroTipo.isEmpty()) {
            return movimientoRepository.listarPorTipo(filtroTipo);
        }
        if (filtroProductoId != null && !filtroProductoId.isEmpty()) {
            return movimientoRepository.listarPorProducto(Integer.parseInt(filtroProductoId));
        }
        return movimientoRepository.listar();
    }

    public List<Producto> listarProductosParaFormulario() {
        return productoRepository.listar();
    }
}
