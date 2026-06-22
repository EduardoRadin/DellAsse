package com.dellasse.backend.models;

import java.util.List;
import java.util.stream.Collectors;

import com.dellasse.backend.contracts.image.ImageCreateRequest;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa uma galeria de imagens no sistema.
 * <p>
 * Contém informações sobre o nome da galeria, as imagens associadas,
 * a empresa proprietária e as festas relacionadas à galeria.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Gallery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @ManyToOne
    private Enterprise enterprise;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Party> partys;


    public Gallery(Long id) {
        this.id = id;
    }

    public Gallery(String name, List<ImageCreateRequest> images, List<Party> partys) {
        this.name = name;
        if (images != null) {
            this.images = images.stream()
                                .map(dto -> new Image(dto.url(), dto.alt(), this))
                                .collect(Collectors.toList());
        }
        if (partys != null){
            this.partys = partys.stream()
                                .map(party -> new Party(party.getId(), party.getTitle(), party.getDescription(), party.getObservations(), null, name, name, party.getGenerateBudget(), party.getUser(), enterprise, party.getProducts(), this))
                                .collect(Collectors.toList());
        }
    }
}
