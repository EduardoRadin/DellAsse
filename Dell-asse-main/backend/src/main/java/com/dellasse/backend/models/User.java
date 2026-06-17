package com.dellasse.backend.models;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.dellasse.backend.contracts.user.UserLoginRequest;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa um usuário no sistema.
 * <p>
 * Contém informações como nome, e-mail, nome de usuário,
 * senha, telefone, data de nascimento, status ativo,
 * papéis (roles) associados e a empresa vinculada ao usuário.
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users") 
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private String name;
    private String email;
    private String username;
    private String password;
    private String phone;
    private LocalDate birthday;
    private boolean active;

    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
    
    @ManyToOne
    private Enterprise enterprise;

    public User(UUID uuid){
        this.uuid = uuid;
    }

    public User(String name, String email, String username, String password, LocalDate birthday, String phone, boolean active, List<Role> listRole, Enterprise enterprise){
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.birthday = birthday;
        this.phone = phone;
        this.active = active;
        this.roles = listRole;
        this.enterprise = enterprise;
    }

    public User(String name, String email, String username, String password, String phone, LocalDate birthday){
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
    }  

    public boolean isLoginCorret(UserLoginRequest user, PasswordEncoder passwordEncoder){
       return passwordEncoder.matches(user.password(), this.password);
    }
}