package com.dellasse.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellasse.backend.contracts.user.UserCreateRequest;
import com.dellasse.backend.contracts.user.UserResponse;
import com.dellasse.backend.contracts.user.UserLoginRequest;
import com.dellasse.backend.contracts.user.UserUpdateRequest;
import com.dellasse.backend.service.UserService;

import jakarta.validation.Valid;

/**
 * Controlador REST para gerenciar os Usuários.
 * <p>
 * Este controlador fornece endpoints para criar, atualizar e consultar usuários.
 *
 * @author  Dell'Asse
 * @version 1.0
 * @since 2025-11-21
 */

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private UserService userService;

    /**
     * Cria um novo Usuário.
     *
     * @param request DTO contendo os dados do Usuário a ser criado.
     * @param token   Token JWT do usuário autenticado.
     * @return ResponseEntity com o resultado da operação.
     */
    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserCreateRequest request, JwtAuthenticationToken token) {
        String authenticatedUserId = (token != null) ? token.getName() : null;
        return userService.createUser(request, authenticatedUserId);
    }

     /**
     * Realiza o login de um Usuário.
     *
     * @param loginRequest DTO contendo os dados de login do Usuário.
     * @return ResponseEntity com o resultado da operação.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid UserLoginRequest loginRequest) {
        return userService.loginUser(loginRequest);
    }

     /**
     * Atualiza os dados de um Usuário existente.
     *
     * @param request DTO contendo os dados atualizados do Usuário.
     * @param userId  O ID do Usuário a ser atualizado.
     * @param token   Token JWT do usuário autenticado.
     * @return ResponseEntity com o resultado da operação.
     */
    @PostMapping("/update/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateRequest request, @PathVariable UUID userId, JwtAuthenticationToken token) {
        return userService.updateUser(request, userId, token.getName());
    }

    /**
     * Retorna uma lista com todos os usuários cadastrados.
     * Deve ser acessado apenas por ADMIN.
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return userService.getAllUsers();
    }
}
