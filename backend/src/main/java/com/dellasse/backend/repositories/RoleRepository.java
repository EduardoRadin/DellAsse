package com.dellasse.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dellasse.backend.models.Role;

/**
 * Repositório para a entidade Role.
 * <p>
 * Fornece métodos para operações de CRUD e consultas relacionadas aos papéis (roles).
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
