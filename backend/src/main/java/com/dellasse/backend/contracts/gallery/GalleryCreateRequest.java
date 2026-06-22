package com.dellasse.backend.contracts.gallery;

import java.util.List;

import com.dellasse.backend.contracts.image.ImageCreateRequest;

import jakarta.validation.constraints.NotBlank;

/**
 * Representa o objeto de transferência de dados (DTO) para a criação de uma nova Galeria.
 * <p>
 * Este record encapsula os dados necessários que são enviados pela API
 * para registrar uma nova galeria no sistema.
 *
 * @param name        O nome oficial da galeria.
 * @param description Uma descrição opcional da galeria.
 * @param imageUrl    Uma lista de URLs de imagens que compõem a galeria.
 */
public record GalleryCreateRequest(
        @NotBlank String name, 
        String description,
        List<ImageCreateRequest> imageUrl
) {}
    