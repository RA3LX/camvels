package com.camvels.application.usecase;

import com.camvels.application.port.in.UserPort;
import com.camvels.domain.model.RolUsuario;
import com.camvels.domain.model.Usuario;
import com.camvels.domain.port.out.UsuarioRepository;
import java.util.List;
import java.util.Optional;

public class UserUseCase implements UserPort {

    private final UsuarioRepository usuarioRepository;

    public UserUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listar() {
        return usuarioRepository.listar();
    }

    public Optional<Usuario> buscarPorId(int id) {
        return usuarioRepository.buscarPorId(id);
    }

    public boolean guardar(Usuario usuario) {
        if (!RolUsuario.esValido(usuario.getRol())) {
            return false;
        }
        if (usuario.getId() == 0) {
            return usuarioRepository.agregar(usuario);
        }
        return usuarioRepository.actualizar(usuario);
    }

    public boolean eliminar(int id) {
        return usuarioRepository.eliminar(id);
    }
}
