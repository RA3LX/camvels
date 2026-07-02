package com.camvels.infrastructure.adapter.in.web;

import com.camvels.application.port.in.CategoryPort;
import com.camvels.domain.model.Categoria;
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
@RequestMapping("/api/categories")
@PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
public class CategoryController {

    private final CategoryPort categoryPort;

    public CategoryController(CategoryPort categoryPort) {
        this.categoryPort = categoryPort;
    }

    @GetMapping
    public List<Categoria> list() {
        return categoryPort.listar();
    }

    @GetMapping("/{id}")
    public Categoria get(@PathVariable int id) {
        return categoryPort.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public Categoria create(@RequestBody Categoria categoria) {
        if (!categoryPort.guardar(categoria)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return categoria;
    }

    @PutMapping("/{id}")
    public Categoria update(@PathVariable int id, @RequestBody Categoria categoria) {
        categoria.setId(id);
        if (!categoryPort.guardar(categoria)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return categoryPort.buscarPorId(id).orElse(categoria);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable int id) {
        if (!categoryPort.eliminar(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
