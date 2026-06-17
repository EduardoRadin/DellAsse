package com.dellasse.backend.service;

import java.util.UUID;
import java.util.stream.Collectors;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.dellasse.backend.contracts.product.ProductCreateRequest;
import com.dellasse.backend.contracts.product.ProductResponse;
import com.dellasse.backend.contracts.product.ProductUpdateRequest;
import com.dellasse.backend.contracts.product.ProductUpdateResponse;
import com.dellasse.backend.mappers.ProductMapper;
import com.dellasse.backend.models.Enterprise;
import com.dellasse.backend.models.Product;
import com.dellasse.backend.models.User;
import com.dellasse.backend.repositories.ProductRepository;
import com.dellasse.backend.util.ConvertString;
import com.dellasse.backend.util.DateUtils;

import jakarta.persistence.EntityManager;

import com.dellasse.backend.exceptions.DomainError;
import com.dellasse.backend.exceptions.DomainException;

/**
 * Serviço para a entidade Product.
 * <p>
 * Fornece métodos para operações relacionadas aos produtos.
 */
@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    /**
     * Cria um novo produto.
     *
     * @param createRequest Dados do produto a ser criado.
     * @param token         Token do usuário que está criando o produto.
     * @return Resposta HTTP indicando o resultado da operação.
     */
    public ResponseEntity<?> create(ProductCreateRequest createRequest, String token){
        UUID userId = ConvertString.toUUID(token);

        Product product = ProductMapper.toEntityCreateProduct(createRequest);
        if (product == null){
            throw new NullPointerException();
        }

        UUID enterpriseId = userService.validateUserEnterprise(userId);

        setValueDefault(product, userId, enterpriseId);
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * Define os valores padrão para um produto.
     *
     * @param product      O produto a ser configurado.
     * @param userId       ID do usuário que está criando o produto.
     * @param enterpriseId ID da empresa associada ao produto.
     */
    private void setValueDefault(Product product, UUID userId, UUID enterpriseId){
        product.setUser(entityManager.getReference(User.class, userId));
        product.setEnterprise(entityManager.getReference(Enterprise.class, enterpriseId));
        product.setDateCreate(DateUtils.now());
        product.setDateUpdate(DateUtils.now());
    }

    /**
     * Atualiza um produto existente.
     *
     * @param request   Dados atualizados do produto.
     * @param productId ID do produto a ser atualizado.
     * @param token     Token do usuário que está realizando a atualização.
     * @return Dados do produto atualizado.
     */
    public ProductUpdateResponse update(ProductUpdateRequest request, Long productId, String token) {
        
        if (productId == null){
            throw new DomainException(DomainError.PRODUCT_NOT_FOUND_INTERNAL);
        }

        UUID userId = ConvertString.toUUID(token);
        UUID enterpriseId = userService.validateUserEnterprise(userId);
        Product entity = productRepository.findById(productId)
                .orElseThrow(() -> new DomainException(DomainError.PRODUCT_NOT_FOUND));
        
        if (enterpriseId == null){
            throw new DomainException(DomainError.USER_CANNOT_UPDATE_DATA);
        }

        existsProductEnterprise(productId, enterpriseId);
        setValueUpdate(entity, request);
        productRepository.save(entity);
        return ProductMapper.toContractUpdateResponse(entity);
    }

    /**
     * Verifica se o produto pertence à empresa do usuário.
     *
     * @param productId    ID do produto.
     * @param enterpriseId ID da empresa.
     */
    private void existsProductEnterprise(Long productId, UUID enterpriseId){
        if (enterpriseId == null || productId == null){
            throw new DomainException(DomainError.PRODUCT_OR_ENTERPRISE_NOT_FOUND_INTERNAL);
        }

        if (!productRepository.existsById(productId)){
            throw new DomainException(DomainError.PRODUCT_NOT_FOUND);
        }

        if(!productRepository.existsByIdAndEnterprise_Id(productId, enterpriseId)){
            throw new DomainException(DomainError.PRODUCT_NOT_FOUND);
        }
    }

    /**
     * Define os valores atualizados para um produto.
     *
     * @param entity  O produto a ser atualizado.
     * @param request Dados atualizados do produto.
     */
    private void setValueUpdate(Product entity, ProductUpdateRequest request){
        if (request.name() != null){
            entity.setName(request.name());
        }
        if (request.description() != null){
            entity.setDescription(request.description());
        }
        if (request.price() != null){
            entity.setPrice(request.price());
        }
        if (request.stockQuantity() != null){
            entity.setStockQuantity(request.stockQuantity());
        }
        if (request.category() != null){
            entity.setCategory(request.category());
        }
        if (request.imageUrl() != null){
            entity.setImageUrl(request.imageUrl());
        }
        entity.setDateUpdate(DateUtils.now());
    }

    /**
     * Busca todos os produtos.
     *
     * @return Lista de produtos encontrados.
     */
    public List<ProductResponse> getAll() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca um produto pelo ID, validando o vínculo com a empresa do usuário.
     */
    public ProductResponse getById(Long productId, String token) {
        if (productId == null) {
            throw new DomainException(DomainError.PRODUCT_NOT_FOUND_INTERNAL);
        }

        UUID userId = ConvertString.toUUID(token);
        UUID enterpriseId = userService.validateUserEnterprise(userId);
        if (enterpriseId == null) {
            throw new DomainException(DomainError.ENTERPRISE_NOT_FOUND);
        }

        existsProductEnterprise(productId, enterpriseId);
        Product entity = productRepository.findById(productId)
            .orElseThrow(() -> new DomainException(DomainError.PRODUCT_NOT_FOUND));

        return ProductMapper.toResponse(entity);
    }

    /**
     * Remove um produto pelo ID, validando o vínculo com a empresa do usuário.
     */
    public ResponseEntity<?> delete(Long productId, String token) {
        if (productId == null) {
            throw new DomainException(DomainError.PRODUCT_NOT_FOUND_INTERNAL);
        }

        UUID userId = ConvertString.toUUID(token);
        UUID enterpriseId = userService.validateUserEnterprise(userId);
        if (enterpriseId == null) {
            throw new DomainException(DomainError.ENTERPRISE_NOT_FOUND);
        }

        existsProductEnterprise(productId, enterpriseId);
        productRepository.deleteById(productId);
        return ResponseEntity.noContent().build();
    }
}
