package com.dellasse.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dellasse.backend.contracts.product.ProductCreateRequest;
import com.dellasse.backend.contracts.product.ProductResponse;
import com.dellasse.backend.contracts.product.ProductUpdateRequest;
import com.dellasse.backend.contracts.product.ProductUpdateResponse;
import com.dellasse.backend.exceptions.DomainError;
import com.dellasse.backend.exceptions.DomainException;
import com.dellasse.backend.models.Enterprise;
import com.dellasse.backend.models.Product;
import com.dellasse.backend.models.User;
import com.dellasse.backend.repositories.ProductRepository;

import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserService userService;

    @Mock
    private EntityManager entityManager;

    // --- TESTES DE CRIAÇÃO (CREATE) ---

    @Test
    @DisplayName("Deve criar produto com sucesso quando dados e empresa são válidos")
    void create_Success() {
        // Arrange
        String token = UUID.randomUUID().toString();
        UUID userId = UUID.fromString(token);
        UUID enterpriseId = UUID.randomUUID();

        ProductCreateRequest request = new ProductCreateRequest(
            "Bolo de Chocolate", 
            "Delicioso", 
            50.0, 
            10, 
            "Comida", 
            "img.jpg"
        );

        // Simula que o usuário tem uma empresa válida
        when(userService.validateUserEnterprise(userId)).thenReturn(enterpriseId);
        
        // Simula o EntityManager encontrando as referências (evita NullPointer)
        when(entityManager.getReference(User.class, userId)).thenReturn(new User());
        when(entityManager.getReference(Enterprise.class, enterpriseId)).thenReturn(new Enterprise());

        // Act
        ResponseEntity<?> response = productService.create(request, token);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    // --- TESTES DE ATUALIZAÇÃO (UPDATE) ---

    @Test
    @DisplayName("Deve atualizar produto com sucesso")
    void update_Success() {
        // Arrange
        String token = UUID.randomUUID().toString();
        UUID userId = UUID.fromString(token);
        UUID enterpriseId = UUID.randomUUID();
        Long productId = 1L;

        ProductUpdateRequest updateRequest = new ProductUpdateRequest(
            "Bolo Novo", "Desc Nova", 60.0, 5, "Doce", "new.jpg"
        );

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Bolo Antigo");

        // Mocks
        when(userService.validateUserEnterprise(userId)).thenReturn(enterpriseId);
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        
        // Importante: Simula que o produto pertence à empresa (mockando existsById e existsByIdAndEnterprise)
        when(productRepository.existsById(productId)).thenReturn(true);
        when(productRepository.existsByIdAndEnterprise_Id(productId, enterpriseId)).thenReturn(true);

        // Act
        ProductUpdateResponse response = productService.update(updateRequest, productId, token);

        // Assert
        assertNotNull(response);
        assertEquals("Bolo Novo", response.name()); // Verifica se o nome mudou no retorno
        verify(productRepository, times(1)).save(existingProduct);
    }

    @Test
    @DisplayName("Deve lançar erro se tentar atualizar produto que não existe")
    void update_ProductNotFound() {
        // Arrange
        String token = UUID.randomUUID().toString();
        Long productId = 99L;
        ProductUpdateRequest request = new ProductUpdateRequest("Nome", null, null, null, null, null);

        when(userService.validateUserEnterprise(any())).thenReturn(UUID.randomUUID());
        when(productRepository.findById(productId)).thenReturn(Optional.empty()); // Produto não encontrado

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> {
            productService.update(request, productId, token);
        });

        assertEquals(DomainError.PRODUCT_NOT_FOUND, exception.getError());
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro se o produto existe mas pertence a OUTRA empresa")
    void update_ProductNotBelongToEnterprise() {
        // Arrange
        String token = UUID.randomUUID().toString();
        UUID userEnterpriseId = UUID.randomUUID(); // Empresa do Usuário
        Long productId = 1L;
        ProductUpdateRequest request = new ProductUpdateRequest("Nome", null, null, null, null, null);

        Product product = new Product();
        product.setId(productId);

        // O usuário tem empresa
        when(userService.validateUserEnterprise(any())).thenReturn(userEnterpriseId);
        // O produto existe no banco
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        // Validação interna do método existsProductEnterprise:
        when(productRepository.existsById(productId)).thenReturn(true); 
        // O PULO DO GATO: O produto NÃO pertence à empresa do usuário
        when(productRepository.existsByIdAndEnterprise_Id(productId, userEnterpriseId)).thenReturn(false);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> {
            productService.update(request, productId, token);
        });

        // Esperamos que o sistema diga que o produto não foi encontrado (para segurança) ou acesso negado
        assertEquals(DomainError.PRODUCT_NOT_FOUND, exception.getError());
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar erro se o usuário não tiver empresa vinculada ao tentar atualizar")
    void update_UserWithoutEnterprise() {
        // Arrange
        String token = UUID.randomUUID().toString();
        Long productId = 1L;
        ProductUpdateRequest request = new ProductUpdateRequest("Nome", null, null, null, null, null);

        when(productRepository.findById(productId)).thenReturn(Optional.of(new Product()));
        // Simula usuário sem empresa (null)
        when(userService.validateUserEnterprise(any())).thenReturn(null);

        // Act & Assert
        DomainException exception = assertThrows(DomainException.class, () -> {
            productService.update(request, productId, token);
        });

        assertEquals(DomainError.USER_CANNOT_UPDATE_DATA, exception.getError());
    }

    // --- TESTES DE LEITURA (GET ALL) ---

    @Test
    @DisplayName("Deve retornar lista de produtos")
    void getAll_Success() {
        // Arrange
        Product p1 = new Product(); p1.setName("P1"); p1.setPrice(10.0);
        Product p2 = new Product(); p2.setName("P2"); p2.setPrice(20.0);
        
        when(productRepository.findAll()).thenReturn(List.of(p1, p2));

        // Act
        List<ProductResponse> result = productService.getAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("P1", result.get(0).name());
    }
}