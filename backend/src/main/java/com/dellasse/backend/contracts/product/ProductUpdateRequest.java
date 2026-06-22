package com.dellasse.backend.contracts.product;


 /**
 * Representa o objeto de transferência de dados (DTO) para a atualização de um Produto existente.
 * <p>
 * Este record encapsula os dados necessários que são enviados pela API
 * para atualizar os dados de um produto no sistema.
 *  
 * @param name          O nome do produto. Não pode estar em branco.
 * @param description   Uma descrição opcional do produto.
 * @param price         O preço unitário do produto. Não pode ser nulo e deve ser maior que 0.0.
 * @param stockQuantity A quantidade em estoque do produto. Não pode ser nula e deve ser maior que 0.
 * @param category      A categoria à qual o produto pertence. Não pode estar em branco.
 * @param imageUrl      Uma URL opcional para a imagem do produto.
 */

public record ProductUpdateRequest(

    String name,
    String description,
    Double price,
    Integer stockQuantity,
    String imageUrl,
    String category
    
) {}
