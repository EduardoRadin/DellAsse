package com.dellasse.backend.contracts.user;

/**
 * Representa o objeto de transferência de dados (DTO) para a Atualização de um Usuário.
 * <p>
 * Este record encapsula os dados necessários que são enviados pela API
 * para atualizar os dados de um usuário no sistema.
 *
 * @param name          O nome do usuário.
 * @param email         O endereço de e-mail do usuário.
 * @param username      O nome de usuário único.
 * @param password      A senha do usuário.
 * @param confirmPassword A confirmação da senha do usuário.
 * @param active        Indica se o usuário está ativo (true) ou inativo (false).
 */
public record UserUpdateRequest(

    String name,
    String email,
    String username,
    String password,
    String confirmPassword,
    boolean active

) {}
