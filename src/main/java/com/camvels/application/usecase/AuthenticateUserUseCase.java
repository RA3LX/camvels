package com.camvels.application.usecase;

import com.camvels.application.port.in.AuthenticateUserPort;
import com.camvels.domain.model.Usuario;
import com.camvels.domain.port.out.UsuarioRepository;
import java.util.Optional;

public class AuthenticateUserUseCase implements AuthenticateUserPort {

    private final UsuarioRepository usuarioRepository;

    public AuthenticateUserUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Usuario> ejecutar(String usuario, String password) {
        return usuarioRepository.validar(usuario, password);
    }
}
