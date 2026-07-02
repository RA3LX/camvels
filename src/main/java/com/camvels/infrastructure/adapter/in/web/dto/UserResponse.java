package com.camvels.infrastructure.adapter.in.web.dto;

import com.camvels.domain.model.Usuario;

public record UserResponse(int id, String usuario, String nombre, String rol) {

    public static UserResponse from(Usuario usuario) {
        return new UserResponse(usuario.getId(), usuario.getUsuario(), usuario.getNombre(), usuario.getRol());
    }
}
