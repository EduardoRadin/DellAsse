package com.dellasse.backend.mappers;

import com.dellasse.backend.contracts.user.UserCreateRequest;
import com.dellasse.backend.contracts.user.UserResponse;
import com.dellasse.backend.contracts.user.UserUpdateResponse;
import com.dellasse.backend.models.User;

/**
 * Classe responsável por mapear entre entidades User e seus respectivos DTOs.
 * <p>
 * Fornece métodos para converter entre UserCreateRequest, UserUpdateResponse,
 * UserResponse e a entidade User.
 */
public class UserMapper {
    
    /** 
     * Converte um DTO de criação de usuário para a entidade User.
     *
     * @param dtoRequest O DTO contendo os dados para criar um novo usuário.
     * @return A entidade User criada a partir do DTO.
     */
    public static User toEntity(UserCreateRequest dtoRequest) {
        return new User(
            dtoRequest.name(),
            dtoRequest.email(),
            dtoRequest.username(),
            dtoRequest.password(),
            dtoRequest.phone(),
            dtoRequest.birthday()
    
        );
    }

    /** 
     * Converte uma entidade User para o DTO de resposta de atualização.
     *
     * @param user A entidade User a ser convertida.
     * @return O DTO de resposta de atualização contendo os dados do usuário.
     */
    public static UserUpdateResponse toResponse(User user) {
        return new UserUpdateResponse(
                user.getName(),
                user.getEmail(),
                user.getUsername(),
                user.isActive()
            );
    }

    /** 
     * Converte uma entidade User para o DTO de resposta.
     *
     * @param user A entidade User a ser convertida.
     * @return O DTO de resposta contendo os dados do usuário.
     */
    public static UserResponse toUserResponse(User user) {
        return new UserResponse(
            user.getUuid(),
            user.getName(),
            user.getEmail(),
            user.getUsername(),
            user.getPhone(),
            user.getBirthday(),
            user.isActive(),
            user.getRoles()
        );
    }
}
