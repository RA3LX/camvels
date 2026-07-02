package com.camvels.domain.port.out;

import com.camvels.domain.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {
    List<Categoria> listar();
    Optional<Categoria> buscarPorId(int id);
    boolean agregar(Categoria categoria);
    boolean actualizar(Categoria categoria);
    boolean eliminar(int id);
}
