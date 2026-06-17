package com.dellasse.backend.mappers;

import com.dellasse.backend.contracts.product.ProductCreateRequest;
import com.dellasse.backend.contracts.product.ProductResponse;
import com.dellasse.backend.contracts.product.ProductUpdateRequest;
import com.dellasse.backend.contracts.product.ProductUpdateResponse;
import com.dellasse.backend.models.Product;

/**
 * Classe responsável por mapear entre entidades Product e seus respectivos DTOs.
 * <p>
 * Fornece métodos para converter entre ProductCreateRequest, ProductUpdateRequest,
 * ProductResponse, ProductUpdateResponse e a entidade Product.
 */
public class ProductMapper {

    /** 
     * Converte um DTO de criação de produto para a entidade Product.
     *
     * @param createRequest O DTO contendo os dados para criar um novo produto.
     * @return A entidade Product criada a partir do DTO.
     */
    public static Product toEntityCreateProduct(ProductCreateRequest createRequest) {
        return new Product(
            createRequest.name(), 
            createRequest.description(), 
            createRequest.price(), 
            createRequest.stockQuantity(), 
            createRequest.category(), 
            createRequest.imageUrl()
        );
    }

    /** 
     * Converte um DTO de atualização de produto para a entidade Product.
     *
     * @param request O DTO contendo os dados para atualizar um produto existente.
     * @return A entidade Product atualizada a partir do DTO.
     */
    public static Product toEntityUpdateProduct(ProductUpdateRequest request) {
        return new Product(
            request.name(), 
            request.description(), 
            request.price(), 
            request.stockQuantity(), 
            request.category(), 
            request.imageUrl()
        );
    }

    /** 
     * Converte uma entidade Product para o DTO de resposta de atualização.
     *
     * @param product A entidade Product a ser convertida.
     * @return O DTO de resposta de atualização contendo os dados do produto.
     */
    public static ProductUpdateResponse toContractUpdateResponse(Product product) {
        return new ProductUpdateResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStockQuantity(),
            product.getImageUrl()
        );
    }

    /** 
     * Converte uma entidade Product para o DTO de resposta.
     *
     * @param product A entidade Product a ser convertida.
     * @return O DTO de resposta contendo os dados do produto.
     */
    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStockQuantity(),
            product.getCategory(),
            product.getImageUrl()
        );
    }
    
}