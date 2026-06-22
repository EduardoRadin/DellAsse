package com.dellasse.backend.contracts.product;


/**
 * Representa o objeto de transferência de dados (DTO) para a resposta de uma atualização de um produto.
 * <p>
 * Este record encapsula os dados necessários que são enviados pela API
 * para recuperar os dados de uma atualização de um produto.
 *
 * @param id             O id do produto.
 * @param name           O nome do produto.
 * @param description    Uma descrição opcional do produto.
 * @param price          O preço unitário do produto. Não pode ser nulo e deve ser maior que 0.0.
 * @param stockQuantity  A quantidade em estoque do produto. Não pode ser nula e deve ser maior que 0.
 * @param imageUrl       Uma URL opcional para a imagem do produto.
 */
public record ProductUpdateResponse(
    
    Long id,
    String name,
    String description,
    Double price,
    Integer stockQuantity,
    String imageUrl

) {}
