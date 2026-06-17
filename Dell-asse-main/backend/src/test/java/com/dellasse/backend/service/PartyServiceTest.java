package com.dellasse.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dellasse.backend.contracts.party.PartyResponse;
import com.dellasse.backend.models.Party;
import com.dellasse.backend.models.Role;
import com.dellasse.backend.repositories.PartyRepository;

@ExtendWith(MockitoExtension.class)
class PartyServiceTest {

    @InjectMocks
    private PartyService partyService;

    @Mock
    private PartyRepository partyRepository;

    @Mock
    private UserService userService;

    @Test
    @DisplayName("Deve retornar TODAS as festas se o usuário for ADMIN")
    void getAll_AsAdmin() {
        // Arrange
        String tokenUuid = UUID.randomUUID().toString();
        UUID userId = UUID.fromString(tokenUuid);

        // Simula que o usuário tem papel de ADMIN
        Role adminRole = new Role(1L, "ADMIN");
        when(userService.getRoles(userId)).thenReturn(List.of(adminRole));

        // CORREÇÃO: Criar festas com lista de produtos vazia (evita NullPointerException no Mapper)
        Party p1 = new Party();
        p1.setProducts(Collections.emptyList()); 
        
        Party p2 = new Party();
        p2.setProducts(Collections.emptyList());

        when(partyRepository.findAll()).thenReturn(List.of(p1, p2));

        // Act
        List<PartyResponse> result = partyService.getAll(tokenUuid);

        // Assert
        assertEquals(2, result.size());
        verify(partyRepository, times(1)).findAll(); 
        verify(userService, never()).validateUserEnterprise(any()); 
    }

    @Test
    @DisplayName("Deve retornar APENAS festas do usuário se for CLIENTE comum")
    void getAll_AsClient() {
        // Arrange
        String tokenUuid = UUID.randomUUID().toString();
        UUID userId = UUID.fromString(tokenUuid);

        // Simula que é cliente (BASIC) e NÃO tem empresa
        Role clientRole = new Role(3L, "BASIC");
        when(userService.getRoles(userId)).thenReturn(List.of(clientRole));
        when(userService.validateUserEnterprise(userId)).thenReturn(null); 

        // CORREÇÃO: Inicializar lista de produtos
        Party p1 = new Party();
        p1.setProducts(Collections.emptyList());

        when(partyRepository.findAllByUser_Uuid(userId)).thenReturn(List.of(p1));

        // Act
        List<PartyResponse> result = partyService.getAll(tokenUuid);

        // Assert
        assertEquals(1, result.size());
        verify(partyRepository, times(1)).findAllByUser_Uuid(userId); 
        verify(partyRepository, never()).findAll(); 
    }
}