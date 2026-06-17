package com.dellasse.backend.contracts.user;

/**
 * Representa o objeto de transferência de dados (DTO) para a resposta de Atualização de um Usuário.
 * <p>
 * Este record encapsula os dados retornados pela API
 * após a atualização dos dados de um usuário no sistema.
 *
 * @param name      O nome do usuário.
 * @param email     O endereço de e-mail do usuário.
 * @param username  O nome de usuário único.
 * @param isActive  Indica se o usuário está ativo (true) ou inativo (false).
 */

public record UserUpdateResponse(
    String name,
    String email,
    String username,
    boolean isActive
){}
