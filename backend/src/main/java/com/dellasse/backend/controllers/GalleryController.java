package com.dellasse.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellasse.backend.contracts.gallery.GalleryCreateRequest;
import com.dellasse.backend.contracts.gallery.GalleryResponse;
import com.dellasse.backend.service.GalleryService;

import jakarta.validation.Valid;

/**
 * Controlador REST para gerenciar a galeria de imagens.
 * <p>
 * Este controlador fornece endpoints para criar e recuperar imagens da galeria.
 *
 * @author  Dell'Assa
 * @version 1.0
 * @since 2025-11-21
 */
@RestController
@RequestMapping("/gallery")
public class GalleryController {

    @Autowired
    private GalleryService galleryService;

    /**
     * Cria uma nova galeria.
     *
     * @param request DTO contendo os dados da galeria a ser criada.
     * @param token   Token JWT do usuário autenticado.
     */
    @PostMapping("/create")
    public void create(@Valid @RequestBody GalleryCreateRequest request, JwtAuthenticationToken token) {
        galleryService.create(request, token.getName());
    }

    // Compatibilidade com o front-end atual (POST /gallery)
    @PostMapping
    public void createCompat(@Valid @RequestBody GalleryCreateRequest request, JwtAuthenticationToken token) {
        galleryService.create(request, token.getName());
    }

     /**
     * Retorna todas as galeria.
     *
     * @param token Token JWT do usuário autenticado.
     * @return Lista de DTOs contendo os dados das galeria.
     */
    @GetMapping("/all")
    public List<GalleryResponse> getAll(JwtAuthenticationToken token) {
        return galleryService.findAll(token.getName());
    }

    // Compatibilidade com o front-end atual (GET /gallery)
    @GetMapping
    public List<GalleryResponse> getAllCompat(JwtAuthenticationToken token) {
        return galleryService.findAll(token.getName());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, JwtAuthenticationToken token) {
        galleryService.delete(id, token.getName());
    }
}
