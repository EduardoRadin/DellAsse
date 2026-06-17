package com.dellasse.backend.contracts.user;

import jakarta.validation.constraints.NotBlank;

/**
 * Representa o objeto de transferência de dados (DTO) para a solicitação de login de um usuário.
 * <p>
 * Este record encapsula os dados necessários que são enviados pela API
 * para autenticar um usuário e obter um token de acesso.
 *
 * @param username O nome de usuário único.
 * @param password A senha do usuário.
 */
public record UserLoginRequest(

    @NotBlank(message = "Username is required") 
    String username, 
    
    @NotBlank(message = "Password is required") 
    String password
                           
) {}
