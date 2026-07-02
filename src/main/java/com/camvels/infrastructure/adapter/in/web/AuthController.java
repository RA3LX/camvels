package com.camvels.infrastructure.adapter.in.web;

import com.camvels.application.port.in.AuthenticateUserPort;
import com.camvels.domain.model.Usuario;
import com.camvels.infrastructure.adapter.in.web.dto.LoginRequest;
import com.camvels.infrastructure.adapter.in.web.dto.LoginResponse;
import com.camvels.infrastructure.adapter.in.web.dto.UserResponse;
import com.camvels.infrastructure.security.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticateUserPort authenticateUserPort;
    private final JwtService jwtService;

    public AuthController(AuthenticateUserPort authenticateUserPort, JwtService jwtService) {
        this.authenticateUserPort = authenticateUserPort;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        Usuario usuario = authenticateUserPort.ejecutar(request.usuario(), request.password())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos"));
        String token = jwtService.generateToken(usuario);
        return new LoginResponse(token, UserResponse.from(usuario));
    }

    @GetMapping("/me")
    public UserResponse me() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getDetails() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        Claims claims = (Claims) auth.getDetails();
        return new UserResponse(
                Integer.parseInt(claims.getSubject()),
                claims.get("usuario", String.class),
                claims.get("nombre", String.class),
                claims.get("rol", String.class)
        );
    }
}
