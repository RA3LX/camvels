package com.camvels.application.usecase;

import com.camvels.application.port.in.SupplierPort;
import com.camvels.domain.model.Proveedor;
import com.camvels.domain.port.out.ProveedorRepository;
import java.util.List;
import java.util.Optional;

public class SupplierUseCase implements SupplierPort {

    private final ProveedorRepository proveedorRepository;

    public SupplierUseCase(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<Proveedor> listar() {
        return proveedorRepository.listar();
    }

    public Optional<Proveedor> buscarPorId(int id) {
        return proveedorRepository.buscarPorId(id);
    }

    public boolean guardar(Proveedor proveedor) {
        if (proveedor.getId() == 0) {
            return proveedorRepository.agregar(proveedor);
        }
        return proveedorRepository.actualizar(proveedor);
    }

    public boolean eliminar(int id) {
        return proveedorRepository.eliminar(id);
    }
}
