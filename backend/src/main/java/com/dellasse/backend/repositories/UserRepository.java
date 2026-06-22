package com.dellasse.backend.repositories;
import com.dellasse.backend.models.Role;
import  com.dellasse.backend.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;  

/**
 * Repositório para a entidade User.
 * <p>
 * Fornece métodos para operações de CRUD e consultas relacionadas aos usuários.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID>  {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByUuidAndRoles_Id(UUID id, Long role);
    boolean existsByUuidAndEnterprise_Id(UUID userId, UUID enterpriseId);
    
    @Query("SELECT u.roles FROM User u WHERE u.uuid = :uuid")
    List<Role> findRoleByUuid(UUID uuid);
    @Query("SELECT u.enterprise.id FROM User u WHERE u.uuid = :uuid")
    UUID findEnterpriseIdByUuid(UUID uuid);
}
