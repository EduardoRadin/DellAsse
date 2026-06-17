package com.dellasse.backend.contracts.gallery;

import java.util.List;

import com.dellasse.backend.contracts.image.ImageCreateRequest;
import com.dellasse.backend.contracts.party.PartyResponse;

import jakarta.validation.constraints.NotBlank;
/**
 * Representa o objeto de transferência de dados (DTO) para a resposta de uma Galeria.
 * <p>
 * Este record encapsula os dados necessários que são enviados pela API
 * para recuperar os dados de uma galeria.
 *
 * @param name        O nome oficial da galeria.
 * @param images      Uma lista de imagens que compõem a galeria.
 * @param partys      Uma lista de party associadas à galeria.
 */
public record GalleryResponse(
    
    Long id,
    @NotBlank String name,
    List<ImageCreateRequest> images,
    List<PartyResponse> partys

) {}
