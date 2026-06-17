package com.dellasse.backend.mappers;

import com.dellasse.backend.contracts.image.ImageCreateRequest;
import com.dellasse.backend.models.Gallery;
import com.dellasse.backend.models.Image;

/**
 * Classe responsável por mapear entre entidades Image e seus respectivos DTOs.
 * <p>
 * Fornece métodos para converter entre ImageCreateRequest e a entidade Image.
 */
public class ImageMapper {

    /** 
     * Converte um DTO de criação de imagem para a entidade Image.
     *
     * @param request O DTO contendo os dados para criar uma nova imagem.
     * @param gallery A entidade Gallery associada à imagem.
     * @return A entidade Image criada a partir do DTO.
     */
    public static Image toEntity(ImageCreateRequest request, Gallery gallery) {
        return new Image(
            request.url(),
            request.alt(),
            gallery
        );
    }
}
