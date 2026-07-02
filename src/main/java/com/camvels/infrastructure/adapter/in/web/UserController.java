package com.camvels.infrastructure.adapter.in.web;

import com.camvels.application.port.in.UserPort;
import com.camvels.domain.model.Usuario;
import com.camvels.infrastructure.adapter.in.web.dto.UserResponse;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final UserPort userPort;

    public UserController(UserPort userPort) {
        this.userPort = userPort;
    }

    @GetMapping
    public List<Usuario> list() {
        return userPort.listar();
    }

    @GetMapping("/{id}")
    public Usuario get(@PathVariable int id) {
        return userPort.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public UserResponse create(@RequestBody Usuario usuario) {
        if (!userPort.guardar(usuario)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return UserResponse.from(usuario);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable int id, @RequestBody Usuario usuario) {
        usuario.setId(id);
        if (!userPort.guardar(usuario)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return userPort.buscarPorId(id).map(UserResponse::from).orElse(UserResponse.from(usuario));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        if (!userPort.eliminar(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
