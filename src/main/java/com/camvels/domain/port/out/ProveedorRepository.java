package com.camvels.domain.port.out;

import com.camvels.domain.model.Proveedor;
import java.util.List;
import java.util.Optional;

public interface ProveedorRepository {
    List<Proveedor> listar();
    Optional<Proveedor> buscarPorId(int id);
    boolean agregar(Proveedor proveedor);
    boolean actualizar(Proveedor proveedor);
    boolean eliminar(int id);
}
