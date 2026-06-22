package com.dellasse.backend.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.dellasse.backend.contracts.gallery.GalleryCreateRequest;
import com.dellasse.backend.contracts.gallery.GalleryResponse;
import com.dellasse.backend.contracts.image.ImageCreateRequest;
import com.dellasse.backend.contracts.party.PartyResponse;
import com.dellasse.backend.contracts.product.ProductResponse;
import com.dellasse.backend.models.Gallery;
import com.dellasse.backend.models.Image;
import com.dellasse.backend.models.Party;

/**
 * Classe responsável por mapear entre entidades Gallery e seus respectivos DTOs.
 * <p>
 * Fornece métodos para converter entre GalleryCreateRequest, GalleryResponse e a entidade Gallery.
 */
public class GalleryMapper {
    
    /** * Converte um DTO de criação de galeria para a entidade Gallery.
     *
     * @param request O DTO contendo os dados para criar uma nova galeria.
     * @return A entidade Gallery criada a partir do DTO.
     */
    public static Gallery toEntity(GalleryCreateRequest request) {
        Gallery gallery = new Gallery();
        gallery.setName(request.name());
        return gallery;
    }

    /** * Converte uma entidade Gallery para o DTO de resposta.
     *
     * @param gallery A entidade Gallery a ser convertida.
     * @return O DTO de resposta contendo os dados da galeria.
     */
    public static GalleryResponse toResponse(Gallery gallery) {
        List<ImageCreateRequest> images = new ArrayList<>();
        if (gallery.getImages() != null) {
            for (Image img : gallery.getImages()) {
                images.add(new ImageCreateRequest(img.getUrl(), img.getAlt()));
            }
        }
        
        List<PartyResponse> partys = new ArrayList<>();
        if (gallery.getPartys() != null) {
            for (Party party : gallery.getPartys()) {
                List<ProductResponse> products = new ArrayList<>();
                if (party.getProducts() != null) {
                    products = party.getProducts().stream()
                        .map(ProductMapper::toResponse)
                        .collect(Collectors.toList());
                }
                
                partys.add(new PartyResponse(
                    party.getId(),
                    party.getTitle(),
                    party.getObservations(),
                    party.getGenerateBudget(), 
                    party.getImgExample(),
                    party.getStatus(),
                    party.getUser() != null ? party.getUser().getName() : "Usuário Desconhecido",
                    party.getUser() != null ? party.getUser().getPhone() : null,
                    party.getLastAtualization(),
                    products
                ));
            }
        }
        
        return new GalleryResponse(
            gallery.getId(),
            gallery.getName(),
            images,
            partys
        );
    }

    /** * Converte uma lista de entidades Gallery para uma lista de DTOs de resposta.
     *
     * @param galleries A lista de entidades Gallery a ser convertida.
     * @return A lista de DTOs de resposta contendo os dados das galerias.
     */
    public static List<GalleryResponse> toResponseList(List<Gallery> galleries) {
        List<GalleryResponse> list = new ArrayList<>();
        if (galleries != null) {
            for (Gallery gallery : galleries) {
                list.add(toResponse(gallery));
            }
        }
        return list;
    }
}