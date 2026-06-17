package com.dellasse.backend.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dellasse.backend.models.Product;

/**
 * Repositório para a entidade Product.
 * <p>
 * Fornece métodos para operações de CRUD e consultas relacionadas aos produtos.
 */
public interface ProductRepository extends JpaRepository<Product, Long>{
        boolean existsByIdAndEnterprise_Id(Long id, UUID enterpriseId);
}
