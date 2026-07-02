package com.camvels.infrastructure.security;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

public final class CurrentUser {

    private CurrentUser() {
    }

    public static int id() {
        Claims claims = claims();
        return Integer.parseInt(claims.getSubject());
    }

    public static String rol() {
        return claims().get("rol", String.class);
    }

    public static boolean hasRol(String rol) {
        return rol() != null && rol().equalsIgnoreCase(rol);
    }

    public static boolean isAdmin() {
        return hasRol("admin");
    }

    private static Claims claims() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getDetails() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return (Claims) auth.getDetails();
    }
}
