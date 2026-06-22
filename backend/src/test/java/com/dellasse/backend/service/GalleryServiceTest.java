package com.dellasse.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import com.dellasse.backend.contracts.gallery.GalleryCreateRequest;
import com.dellasse.backend.contracts.gallery.GalleryResponse;
import com.dellasse.backend.contracts.image.ImageCreateRequest;
import com.dellasse.backend.exceptions.DomainError;
import com.dellasse.backend.exceptions.DomainException;
import com.dellasse.backend.models.Enterprise;
import com.dellasse.backend.models.Gallery;
import com.dellasse.backend.repositories.GalleryRepository;
import com.dellasse.backend.repositories.ImageRepository;

import jakarta.persistence.EntityManager;

@ExtendWith(MockitoExtension.class)
class GalleryServiceTest {

    @InjectMocks
    private GalleryService galleryService;

    @Mock
    private GalleryRepository galleryRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UserService userService;

    @Mock
    private EntityManager entityManager;

    @Test
    @DisplayName("Deve lançar erro se usuário não tiver empresa vinculada")
    void create_UserWithoutEnterprise() {
        // Arrange
        String token = UUID.randomUUID().toString();
        UUID userId = UUID.fromString(token);
        GalleryCreateRequest request = new GalleryCreateRequest("Titulo", "Desc", null);

        // Simula retorno NULL (usuário sem empresa)
        when(userService.validateUserEnterprise(userId)).thenReturn(null);

        // Act & Assert
        DomainException ex = assertThrows(DomainException.class, () -> 
            galleryService.create(request, token)
        );

        assertEquals(DomainError.USER_NOT_FOUND_ENTERPRISE, ex.getError());
        verify(galleryRepository, never()).save(any());
    }

}