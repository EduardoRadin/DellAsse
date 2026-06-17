package com.dellasse.backend.contracts.party;

import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Representa o objeto de transferência de dados (DTO) para a criação de uma nova Festa (Party).
 * <p>
 * Este record encapsula os dados necessários que são enviados pela API
 * para registrar uma nova festa no sistema.
 *
 * @param title          O título ou nome da festa. Não pode estar em branco.
 * @param description    Uma descrição detalhada da festa. Não pode estar em branco.
 * @param products       Uma lista de IDs de produtos que serão associados a esta festa.
 * @param generateBudget O orçamento gerado/estimado para a festa. Não pode ser nulo e deve ser 0.0 ou maior.
 * @param observations   Notas ou observações adicionais sobre a festa (opcional).
 * @param imageURL       A URL para uma imagem de capa da festa (opcional).
 */
public record PartyCreateRequest(

    @NotBlank(message = "Title is required")
    String title,

    @NotBlank(message = "Description is required")
    String description,

    List<Long> products,

    @NotNull(message = "Generate Budget is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Generate Budget must be zero or greater")
    Double generateBudget,

    String observations,
    String imageURL,
    Long galleryId

) {}


