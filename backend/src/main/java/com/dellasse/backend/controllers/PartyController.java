package com.dellasse.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellasse.backend.contracts.party.PartyBudgetPreviewRequest;
import com.dellasse.backend.contracts.party.PartyBudgetResponse;
import com.dellasse.backend.contracts.party.PartyCreateRequest;
import com.dellasse.backend.contracts.party.PartyResponse;
import com.dellasse.backend.models.Party;
import com.dellasse.backend.service.PartyService;

import jakarta.validation.Valid;

/**
 * Controlador REST para gerenciar as Festas (Parties).
 * <p>
 * Este controlador fornece endpoints para criar, atualizar, consultar e listar festas.
 *
 * @author  Dell'Asse
 * @version 1.0
 * @since 2025-11-21
 */
@RestController
@RequestMapping("/party")
public class PartyController {

    @Autowired
    private PartyService partyService;

    /**
     * Cria uma nova festa (Party).
     *
     * @param createRequest DTO contendo os dados da festa a ser criada.
     * @param token         Token JWT do usuário autenticado.
     * @return DTO contendo os dados da festa criada.
     */
    @PostMapping("/create")
    public Party create(@Valid @RequestBody PartyCreateRequest createRequest, JwtAuthenticationToken token){
        return partyService.create(createRequest, token.getName());
    }

    /**
     * Atualiza os dados de uma festa existente.
     *
     * @param id            O ID da festa a ser atualizada.
     * @param updateRequest DTO contendo os dados atualizados da festa.
     * @param token         Token JWT do usuário autenticado.
     * @return DTO contendo os dados da festa atualizada.
     */
    @PatchMapping("/{id}")
    public PartyResponse update(@PathVariable Long id, @Valid @RequestBody com.dellasse.backend.contracts.party.PartyUpdateRequest updateRequest, JwtAuthenticationToken token){
        return partyService.update(id, updateRequest, token.getName());
    }

    /**
     * Retorna os dados de uma festa (Party) pelo ID informado.
     *
     * @param id    ID da festa.
     * @param token Token JWT do usuário autenticado.
     * @return DTO contendo os dados da festa.
     */
    @GetMapping("/{id}")
    public PartyResponse getById(@PathVariable Long id, JwtAuthenticationToken token){
        return partyService.getById(id, token.getName());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, JwtAuthenticationToken token) {
        partyService.delete(id, token.getName());
    }

     /**
     * Retorna uma lista com todas as festas (Party) cadastradas.
     *
     * @param token Token JWT do usuário autenticado.
     * @return Lista de DTOs contendo os dados de todas as festas.
     */
    @GetMapping("/all")
    public List<PartyResponse> getAll(JwtAuthenticationToken token){
        return partyService.getAll(token.getName());
    }

    /**
     * Atualiza o status de uma festa (Party).
     *
     * @param id     ID da festa.
     * @param status Novo status da festa.
     * @param token  Token JWT do usuário autenticado.
     */
    @PatchMapping("/{id}/status")
    public void updateStatus(@PathVariable Long id, @RequestBody String status, JwtAuthenticationToken token) {
        partyService.updateStatus(id, status, token.getName());
    }
    @GetMapping("/gallery")
    public List<PartyResponse> getPublicGallery() {
        return partyService.getPublicGallery();
    }

    /**
     * Calcula o orçamento de uma festa aplicando as regras de desconto (Strategy).
     */
    @PostMapping("/budget-preview")
    public PartyBudgetResponse previewBudget(@Valid @RequestBody PartyBudgetPreviewRequest request) {
        return partyService.previewBudget(request);
    }
}
