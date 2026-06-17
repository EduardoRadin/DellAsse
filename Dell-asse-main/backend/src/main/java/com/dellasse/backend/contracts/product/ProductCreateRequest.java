package com.dellasse.backend.contracts.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Representa o objeto de transferência de dados (DTO) para a criação de um novo Produto.
 * <p>
 * Este record encapsula os dados necessários que são enviados pela API
 * para registrar um novo produto no sistema.
 *
 * @param name          O nome do produto. Não pode estar em branco.
 * @param description   Uma descrição opcional do produto.
 * @param price         O preço unitário do produto. Não pode ser nulo e deve ser maior que 0.0.
 * @param stockQuantity A quantidade em estoque do produto. Não pode ser nula e deve ser maior que 0.
 * @param category      A categoria à qual o produto pertence. Não pode estar em branco.
 * @param imageUrl      Uma URL opcional para a imagem do produto.
 */
public record ProductCreateRequest(

    @NotBlank(message = "Name is required")
    String name,

    String description,

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
    Double price,

    @NotNull(message = "Stock Quantity is required")
    @DecimalMin(value = "0", inclusive = false, message = "Stock Quantity must be greater than zero")
    Integer stockQuantity,

    @NotBlank(message = "Category is required")
    String category,
    
    String imageUrl
    
) {}
