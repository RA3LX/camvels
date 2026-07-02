package com.camvels.domain.model;

public enum TipoMovimiento {
    ENTRADA,
    SALIDA,
    AJUSTE;

    public static TipoMovimiento fromString(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Tipo de movimiento requerido");
        }
        return TipoMovimiento.valueOf(value.trim().toUpperCase());
    }
}
