package com.camvels.infrastructure.adapter.in.web;

import com.camvels.application.port.in.SupplierPort;
import com.camvels.domain.model.Proveedor;
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
@RequestMapping("/api/suppliers")
public class SupplierController {

    private final SupplierPort supplierPort;

    public SupplierController(SupplierPort supplierPort) {
        this.supplierPort = supplierPort;
    }

    @GetMapping
    public List<Proveedor> list() {
        return supplierPort.listar();
    }

    @GetMapping("/{id}")
    public Proveedor get(@PathVariable int id) {
        return supplierPort.buscarPorId(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public Proveedor create(@RequestBody Proveedor proveedor) {
        if (!supplierPort.guardar(proveedor)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return proveedor;
    }

    @PutMapping("/{id}")
    public Proveedor update(@PathVariable int id, @RequestBody Proveedor proveedor) {
        proveedor.setId(id);
        if (!supplierPort.guardar(proveedor)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return supplierPort.buscarPorId(id).orElse(proveedor);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable int id) {
        if (!supplierPort.eliminar(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
