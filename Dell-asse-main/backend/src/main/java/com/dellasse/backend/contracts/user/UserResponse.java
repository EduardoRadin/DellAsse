package com.dellasse.backend.contracts.user;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.dellasse.backend.models.Role;
/**
 * Representa o objeto de transferência de dados (DTO) para a resposta de informações de um usuário.
 * <p>
 * Este record encapsula os dados que são retornados pela API
 * quando informações sobre um usuário são solicitadas.
 *
 * @param id       O identificador único do usuário.
 * @param name     O nome completo do usuário.
 * @param email    O endereço de e-mail do usuário.
 * @param username O nome de usuário único.
 * @param phone    O número de telefone do usuário.
 * @param birthday A data de nascimento do usuário.
 * @param active   Indica se o usuário está ativo ou inativo.
 * @param roles    A lista de roles associadas ao usuário.
 */
public record UserResponse(
    UUID id,
    String name,
    String email,
    String username,
    String phone,
    LocalDate birthday,
    boolean active,
    List<Role> roles
) {}