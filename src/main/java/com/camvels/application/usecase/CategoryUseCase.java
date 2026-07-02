package com.camvels.application.usecase;

import com.camvels.application.port.in.CategoryPort;
import com.camvels.domain.model.Categoria;
import com.camvels.domain.port.out.CategoriaRepository;
import java.util.List;
import java.util.Optional;

public class CategoryUseCase implements CategoryPort {

    private final CategoriaRepository categoriaRepository;

    public CategoryUseCase(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listar() {
        return categoriaRepository.listar();
    }

    public Optional<Categoria> buscarPorId(int id) {
        return categoriaRepository.buscarPorId(id);
    }

    public boolean guardar(Categoria categoria) {
        if (categoria.getId() == 0) {
            return categoriaRepository.agregar(categoria);
        }
        return categoriaRepository.actualizar(categoria);
    }

    public boolean eliminar(int id) {
        return categoriaRepository.eliminar(id);
    }
}
