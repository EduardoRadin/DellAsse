package com.dellasse.backend.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa um papel (role) no sistema.
 * <p>
 * Contém informações sobre o nome do papel e seu identificador único.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Getter
    public enum Values {
        ADMIN(1L, "ADMIN"),
        FUNCIONARIO(2L, "FUNCIONARIO"),
        BASIC(3L, "BASIC"); 

        private final long roleId;
        private final String name;

        Values(long roleId, String name) {
            this.roleId = roleId;
            this.name = name;
        }

    }
    
}
