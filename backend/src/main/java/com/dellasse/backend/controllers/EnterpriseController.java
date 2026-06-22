package com.dellasse.backend.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellasse.backend.contracts.enterprise.EnterpriseCreateRequest;
import com.dellasse.backend.contracts.enterprise.EnterpriseResponse;
import com.dellasse.backend.contracts.enterprise.EnterpriseUpdateRequest;
import com.dellasse.backend.service.EnterpriseService;

import jakarta.validation.Valid;

/**
 * Controlador REST responsável por operações relacionadas a Empresas.
 * <p>
 * Possibilita criação, atualização, consulta individual e listagem de empresas.
 * O acesso aos endpoints é restrito a usuários com as roles apropriadas.
 *
 * @author  Dell'Asse
 * @version 1.0
 * @since 2025-11-21
 */

@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {
    
    @Autowired
    private EnterpriseService enterpriseService;
    
    /**
     * Cria uma nova Empresa.
     *
     * @param request DTO contendo os dados necessários para a criação da Empresa.
     * @param token   Token JWT do usuário autenticado.
     * @return ResponseEntity&lt;?&gt; representando o resultado da operação.
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(@Valid @org.springframework.web.bind.annotation.RequestBody EnterpriseCreateRequest request, JwtAuthenticationToken token) {
        return enterpriseService.create(request, token.getName()); 
    }

    /**
     * Atualiza os dados de uma Empresa existente.
     *
     * @param request       DTO com os dados atualizados.
     * @param enterpriseId  ID da Empresa a ser modificada.
     * @param token         Token JWT do usuário autenticado.
     * @return ResponseEntity com o resultado da operação.
     */
    @PostMapping("/update/{enterpriseId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@Valid @org.springframework.web.bind.annotation.RequestBody EnterpriseUpdateRequest request, @PathVariable UUID enterpriseId, JwtAuthenticationToken token) {
        return enterpriseService.update(request, enterpriseId, token.getName());
    }

    /**
     * Retorna os dados de uma Empresa pelo ID informado.
     *
     * @param enterpriseId ID da Empresa.
     * @param token        Token JWT do usuário autenticado.
     * @return DTO contendo os dados da Empresa.
     */
    @GetMapping("/{enterpriseId}")
    @PreAuthorize("hasRole('FUNCIONARIO') or hasRole('ADMIN')")
    public EnterpriseResponse findById(@PathVariable UUID enterpriseId, JwtAuthenticationToken token) {
        return enterpriseService.findById(enterpriseId, token.getName());
    }

     /**
     * Retorna uma lista com todos as Empresas cadastradas.
     *
     * @param token Token JWT do usuário autenticado.
     * @return Lista de DTOs contendo os dados das Empresas.
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<EnterpriseResponse> findAll(JwtAuthenticationToken token){
        return enterpriseService.findAll(token.getName());
    }
}
