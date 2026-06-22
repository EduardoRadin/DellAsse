package com.dellasse.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dellasse.backend.models.Cart;

/**
 * Repositório para a entidade Cart.
 * <p>
 * Fornece métodos para operações de CRUD e consultas relacionadas aos carrinhos de compras.
 */
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
}
