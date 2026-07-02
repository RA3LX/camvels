package com.camvels.application.port.in;

import com.camvels.domain.model.Categoria;
import java.util.List;
import java.util.Optional;

public interface CategoryPort {

    List<Categoria> listar();

    Optional<Categoria> buscarPorId(int id);

    boolean guardar(Categoria categoria);

    boolean eliminar(int id);
}
