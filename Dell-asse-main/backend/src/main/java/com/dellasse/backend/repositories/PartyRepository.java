package com.dellasse.backend.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dellasse.backend.models.Party;

/**
 * Repositório para a entidade Party.
 * <p>
 * Fornece métodos para operações de CRUD e consultas relacionadas às festas.
 */
@Repository
public interface PartyRepository extends JpaRepository<Party, Long> {
    List<Party> findAllByEnterprise_Id(UUID enterpriseId);
    List<Party> findAllByStatus(String status);
    List<Party> findAllByUser_Uuid(UUID userId);
}
