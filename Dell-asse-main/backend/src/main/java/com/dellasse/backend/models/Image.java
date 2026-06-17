package com.dellasse.backend.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa uma imagem no sistema.
 * <p>
 * Contém informações sobre a URL da imagem, texto alternativo
 * e a galeria à qual a imagem pertence.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String alt;

    @ManyToOne
    private Gallery gallery;


    public Image(String url, String alt, Gallery gallery) {
        this.url = url;
        this.alt = alt;
        this.gallery = gallery;
    }
}
