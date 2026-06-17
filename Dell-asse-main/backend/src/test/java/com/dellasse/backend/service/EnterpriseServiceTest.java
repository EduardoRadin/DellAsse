package com.dellasse.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.dellasse.backend.contracts.enterprise.EnterpriseCreateRequest;
import com.dellasse.backend.contracts.enterprise.EnterpriseResponse;
import com.dellasse.backend.exceptions.DomainError;
import com.dellasse.backend.exceptions.DomainException;
import com.dellasse.backend.models.Enterprise;
import com.dellasse.backend.models.Role;
import com.dellasse.backend.repositories.EnterpriseRepository;
import com.dellasse.backend.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
class EnterpriseServiceTest {

    @InjectMocks
    private EnterpriseService enterpriseService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EnterpriseRepository enterpriseRepository;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("Deve criar empresa com sucesso se usuário for ADMIN e dados forem únicos")
    void create_Success() {
        // Arrange
        String token = UUID.randomUUID().toString();
        UUID userId = UUID.fromString(token);
        
        EnterpriseCreateRequest request = new EnterpriseCreateRequest(
            "DellAssa Eventos", "12345678000199", "Rua A", "999999999", "email@test.com", "logo.png"
        );

        // 1. Usuário existe
        when(userRepository.existsById(userId)).thenReturn(true);
        // 2. Usuário é ADMIN (Role ID 1)
        when(userRepository.existsByUuidAndRoles_Id(userId, Role.Values.ADMIN.getRoleId())).thenReturn(true);
        // 3. Empresa não existe (nome ou doc)
        when(enterpriseRepository.existsByNameOrDocument(request.name(), request.document())).thenReturn(false);

        // Act
        ResponseEntity<?> response = enterpriseService.create(request, token);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(enterpriseRepository, times(1)).save(any(Enterprise.class));
    }

    @Test
    @DisplayName("Deve impedir criação se o usuário NÃO for ADMIN")
    void create_UserNotAdmin() {
        // Arrange
        String token = UUID.randomUUID().toString();
        UUID userId = UUID.fromString(token);
        EnterpriseCreateRequest request = new EnterpriseCreateRequest("Nome", "Doc", "End", "Tel", "Email", "Logo");

        when(userRepository.existsById(userId)).thenReturn(true);
        // Usuário NÃO é admin (retorna false)
        when(userRepository.existsByUuidAndRoles_Id(userId, Role.Values.ADMIN.getRoleId())).thenReturn(false);

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () -> 
            enterpriseService.create(request, token)
        );
        
        assertEquals(DomainError.USER_NOT_ADMIN, ex.getError());
        verify(enterpriseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve impedir criação se documento ou nome já existirem")
    void create_EnterpriseExists() {
        // Arrange
        String token = UUID.randomUUID().toString();
        UUID userId = UUID.fromString(token);
        EnterpriseCreateRequest request = new EnterpriseCreateRequest("Nome", "Doc", "End", "Tel", "Email", "Logo");

        when(userRepository.existsById(userId)).thenReturn(true);
        when(userRepository.existsByUuidAndRoles_Id(userId, Role.Values.ADMIN.getRoleId())).thenReturn(true);
        // Empresa JÁ existe
        when(enterpriseRepository.existsByNameOrDocument(anyString(), anyString())).thenReturn(true);

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () -> 
            enterpriseService.create(request, token)
        );

        assertEquals(DomainError.ENTERPRISE_EXISTS, ex.getError());
    }

    @Test
    @DisplayName("Deve buscar empresa por ID com sucesso")
    void findById_Success() {
        // Arrange
        String token = UUID.randomUUID().toString();
        UUID userId = UUID.fromString(token);
        UUID enterpriseId = UUID.randomUUID();
        Enterprise enterprise = new Enterprise();
        enterprise.setId(enterpriseId);
        enterprise.setName("Empresa Teste");

        // Simula que o usuário pertence a uma empresa (qualquer uma, a validação é só se não é nulo no serviço atual)
        when(userService.validateUserEnterprise(userId)).thenReturn(enterpriseId);
        when(enterpriseRepository.findById(enterpriseId)).thenReturn(Optional.of(enterprise));

        // Act
        EnterpriseResponse response = enterpriseService.findById(enterpriseId, token);

        // Assert
        assertNotNull(response);
        assertEquals("Empresa Teste", response.name());
    }
}