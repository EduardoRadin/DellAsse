package com.dellasse.backend.contracts.image;

import jakarta.validation.constraints.NotBlank;

/**
 * Representa o objeto de transferência de dados (DTO) para a criação de uma nova Imagem.
 * <p>
 * Este record encapsula os dados necessários que são enviados pela API
 * para registrar uma nova imagem no sistema.
 *
 * @param url A URL da imagem.
 * @param alt Uma alternativa opcional para a imagem.
 */
public record ImageCreateRequest(

    @NotBlank String url, 
    String alt

) {}
