package com.dellasse.backend.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dellasse.backend.contracts.gallery.GalleryCreateRequest;
import com.dellasse.backend.contracts.gallery.GalleryResponse;
import com.dellasse.backend.exceptions.DomainError;
import com.dellasse.backend.exceptions.DomainException;
import com.dellasse.backend.mappers.GalleryMapper;
import com.dellasse.backend.mappers.ImageMapper;
import com.dellasse.backend.models.Enterprise;
import com.dellasse.backend.models.Gallery;
import com.dellasse.backend.models.Image;
import com.dellasse.backend.repositories.GalleryRepository;
import com.dellasse.backend.repositories.ImageRepository;
import com.dellasse.backend.util.ConvertString;

import jakarta.persistence.EntityManager;

/**
 * Serviço para a entidade Gallery.
 * <p>
 * Fornece métodos para operações relacionadas às galerias de imagens.
 */
@Service
public class GalleryService {

    @Autowired
    private GalleryRepository galleryRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    /**
     * Cria uma nova galeria.
     *
     * @param request Dados da galeria a ser criada.
     * @param token   Token do usuário que está criando a galeria.
     */
    public void create(GalleryCreateRequest request, String token) {
        UUID userId = ConvertString.toUUID(token);
        if (userId == null) {
            throw new IllegalArgumentException("Invalid token");
        }
        UUID enterpriseId = userService.validateUserEnterprise(userId);
         if (enterpriseId == null) {
            throw new DomainException(DomainError.USER_NOT_FOUND_ENTERPRISE);
        }

        Gallery gallery = GalleryMapper.toEntity(request);
        gallery.setEnterprise(entityManager.find(Enterprise.class, enterpriseId));
        galleryRepository.save(gallery);
        
        if (request.imageUrl() != null) {
            List<Image> images = request.imageUrl().stream()
                .map(dto -> ImageMapper.toEntity(dto, gallery))
                .collect(Collectors.toList());
            imageRepository.saveAll(images);
        }
    }

    /**
     * Busca todas as galerias.
     *
     * @param token Token do usuário que está realizando a busca.
     * @return Lista de galerias encontradas.
     */
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<GalleryResponse> findAll(String token) {
        List<Gallery> galleries = galleryRepository.findAllWithRelations();
        return GalleryMapper.toResponseList(galleries);
    }

    /**
     * Remove uma galeria pelo ID.
     *
     * @param id    ID da galeria.
     * @param token Token do usuário autenticado.
     */
    public void delete(Long id, String token) {
        UUID userId = ConvertString.toUUID(token);
        UUID enterpriseId = userService.validateUserEnterprise(userId);
        if (enterpriseId == null) {
            throw new DomainException(DomainError.USER_NOT_FOUND_ENTERPRISE);
        }

        Gallery gallery = galleryRepository.findById(id)
            .orElseThrow(() -> new DomainException(DomainError.GALLERY_NOT_FOUND));

        if (gallery.getEnterprise() == null || gallery.getEnterprise().getId() == null) {
            throw new DomainException(DomainError.ENTERPRISE_NOT_FOUND_INTERNAL);
        }

        if (!gallery.getEnterprise().getId().equals(enterpriseId)) {
            throw new DomainException(DomainError.ENTERPRISE_FORBIDDEN);
        }

        galleryRepository.delete(gallery);
    }
}
