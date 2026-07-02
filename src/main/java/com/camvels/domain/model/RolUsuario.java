package com.camvels.domain.model;

public enum RolUsuario {
    ADMIN("admin"),
    ALMACEN("almacen"),
    SUPERVISOR("supervisor");

    private final String valor;

    RolUsuario(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static RolUsuario fromString(String rol) {
        if (rol == null) {
            throw new IllegalArgumentException("Rol requerido");
        }
        for (RolUsuario r : values()) {
            if (r.valor.equals(rol)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Rol no permitido: " + rol);
    }

    public static boolean esValido(String rol) {
        if (rol == null) {
            return false;
        }
        for (RolUsuario r : values()) {
            if (r.valor.equals(rol)) {
                return true;
            }
        }
        return false;
    }
}
