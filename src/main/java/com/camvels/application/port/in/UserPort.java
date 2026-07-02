package com.camvels.application.port.in;

import com.camvels.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UserPort {

    List<Usuario> listar();

    Optional<Usuario> buscarPorId(int id);

    boolean guardar(Usuario usuario);

    boolean eliminar(int id);
}
