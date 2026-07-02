package com.camvels.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MovementRequest(
        @NotBlank String tipo,
        @NotNull @Min(1) Integer productoId,
        @NotNull @Min(1) Integer cantidad,
        String observaciones
) {
}
