package com.dellasse.backend.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dellasse.backend.models.Enterprise;

/**
 * Repositório para a entidade Enterprise.
 * <p>
 * Fornece métodos para operações de CRUD e consultas relacionadas às empresas.
 */
@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, UUID> {
    boolean existsByNameOrDocument(String name, String document);
}
