package com.camvels.infrastructure.adapter.in.web;

import com.camvels.application.port.in.ProductPort;
import com.camvels.domain.model.Producto;
import com.camvels.domain.model.Usuario;
import com.camvels.infrastructure.security.CurrentUser;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductPort productPort;

    public ProductController(ProductPort productPort) {
        this.productPort = productPort;
    }

    @GetMapping
    public List<Producto> list(
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String busqueda) {
        return productPort.listar(categoria, estado, busqueda);
    }

    @GetMapping("/{id}")
    public Producto get(@PathVariable int id) {
        return productPort.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/suppliers")
    public List<?> suppliers() {
        return productPort.listarProveedores();
    }

    @PostMapping
    public Producto create(@RequestBody Producto producto) {
        Usuario usuario = currentUser();
        if (!productPort.guardar(producto, usuario)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo guardar el producto");
        }
        return productPort.buscarPorId(producto.getId()).orElse(producto);
    }

    @PutMapping("/{id}")
    public Producto update(@PathVariable int id, @RequestBody Producto producto) {
        producto.setId(id);
        Usuario usuario = currentUser();
        if (!productPort.guardar(producto, usuario)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo actualizar el producto");
        }
        return productPort.buscarPorId(id).orElse(producto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable int id) {
        if (!productPort.eliminar(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se pudo eliminar el producto");
        }
    }

    private Usuario currentUser() {
        Usuario u = new Usuario();
        u.setId(CurrentUser.id());
        u.setRol(CurrentUser.rol());
        return u;
    }
}
