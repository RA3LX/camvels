package com.camvels.domain.port.out;

import com.camvels.domain.model.Movimiento;
import java.util.List;
import java.util.Map;

public interface MovimientoRepository {
    List<Movimiento> listar();
    List<Movimiento> listarPorTipo(String tipo);
    List<Movimiento> listarPorProducto(int productoId);
    boolean agregar(Movimiento movimiento);
    Map<String, Integer> obtenerMovimientosPorTipo();
    Map<String, Map<String, Integer>> obtenerMovimientosPorFecha(int dias);
    Map<String, Integer> obtenerCantidadPorTipo();
}
