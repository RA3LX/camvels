package com.camvels.infrastructure.adapter.in.web.dto;

public record LoginResponse(String token, UserResponse user) {
}
