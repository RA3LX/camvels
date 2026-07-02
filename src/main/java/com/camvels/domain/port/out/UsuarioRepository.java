package com.camvels.domain.port.out;

import com.camvels.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Optional<Usuario> validar(String usuario, String password);
    List<Usuario> listar();
    Optional<Usuario> buscarPorId(int id);
    boolean agregar(Usuario usuario);
    boolean actualizar(Usuario usuario);
    boolean eliminar(int id);
}
