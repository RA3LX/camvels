package com.camvels.application.port.in;

import com.camvels.domain.model.Usuario;
import java.util.Optional;

public interface AuthenticateUserPort {

    Optional<Usuario> ejecutar(String usuario, String password);
}
