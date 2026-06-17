package com.dellasse.backend.contracts.party;

/**
 * Representa o objeto de transferência de dados (DTO) para a Atualização de uma Festa (Party).
 * <p>
 * Este record encapsula os dados necessários que são enviados pela API
 * para atualizar os dados de uma festa no sistema.
 *
 * @param title          O título ou nome da festa.
 * @param description    Uma descrição detalhada da festa.
 * @param observations   Notas ou observações adicionais sobre a festa (opcional).
 * @param generateBudget O orçamento gerado/estimado para a festa. Não pode ser nulo e deve ser 0.0 ou maior.
 * @param imageURL       A URL para uma imagem de capa da festa (opcional).
 */
public record PartyUpdateRequest(

    String title,
    String description, 
    String observations,
    String imageURL, 
    Double generateBudget

) {}


