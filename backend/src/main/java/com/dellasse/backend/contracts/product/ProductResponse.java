package com.dellasse.backend.contracts.product;

/**
 * Representa o objeto de transferência de dados (DTO) para a resposta de um Produto.
 * <p>
 * Este record encapsula os dados que são enviados pela API
 * ao consultar informações sobre um produto existente no sistema.
 *
 * @param id            O identificador único do produto.
 * @param name          O nome do produto.
 * @param description   A descrição do produto.
 * @param price         O preço unitário do produto.
 * @param stockQuantity A quantidade em estoque do produto.
 * @param category      A categoria à qual o produto pertence.
 * @param imageUrl      A URL da imagem do produto.
 */
public record ProductResponse(
    
    Long id,
    String name, 
    String description,
    Double price,
    Integer stockQuantity,
    String category,
    String imageUrl

) {}
