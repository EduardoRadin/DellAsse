package com.dellasse.backend.contracts.user;

import java.time.LocalDate;

import com.dellasse.backend.validation.PasswordMatches;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

/**
 * Representa o objeto de transferência de dados (DTO) para a criação de um usuário.
 * <p>
 * Este record encapsula os dados necessários que são enviados pela API
 * para criar um novo usuário.
 *
 * @param name          O nome do usuário.
 * @param email         O endereço de e-mail do usuário.
 * @param username      O nome de usuário único.
 * @param password      A senha do usuário.
 * @param confirmPassword A confirmação da senha do usuário.
 */
@PasswordMatches
public record UserCreateRequest(
    
    @NotBlank(message = "Name is required") 
    String name, 
    
    @NotBlank(message = "Email is required")  
    @Email(message = "Invalid email format") 
    String email, 


    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    String username,

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String password,

    @NotBlank(message = "Confirm Password is required")
    @Size(min = 6, message = "Confirm Password must be at least 6 characters long")
    String confirmPassword,

    @Size(min = 11, max = 15, message = "Phone must be between 11 and 15 characters")
    String phone,

    @NotNull(message = "Birthday is required")
    @Past(message = "Birthday must be in the past")
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate birthday

) {}
