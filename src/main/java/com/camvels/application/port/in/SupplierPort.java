package com.camvels.application.port.in;

import com.camvels.domain.model.Proveedor;
import java.util.List;
import java.util.Optional;

public interface SupplierPort {

    List<Proveedor> listar();

    Optional<Proveedor> buscarPorId(int id);

    boolean guardar(Proveedor proveedor);

    boolean eliminar(int id);
}
