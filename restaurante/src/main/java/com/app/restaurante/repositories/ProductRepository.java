package com.app.restaurante.repositories;

import com.app.restaurante.model.Category;
import com.app.restaurante.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Validación de mayúsculas/minúsculas ignoradas requerida por el enunciado
    boolean existsByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);

    // Consulta de filtrado dinámico (Spring Data JPA deriva la consulta de forma automática)
    List<Product> findByCategoryAndAvailable(Category category, Boolean available);
}