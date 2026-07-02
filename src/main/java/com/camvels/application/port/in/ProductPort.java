package com.camvels.application.port.in;

import com.camvels.domain.model.Producto;
import com.camvels.domain.model.Proveedor;
import com.camvels.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface ProductPort {

    List<Producto> listar(String categoria, String estado, String busqueda);

    Optional<Producto> buscarPorId(int id);

    List<Proveedor> listarProveedores();

    boolean guardar(Producto producto, Usuario usuario);

    boolean eliminar(int id);

    List<Producto> listarStockBajo();
}
