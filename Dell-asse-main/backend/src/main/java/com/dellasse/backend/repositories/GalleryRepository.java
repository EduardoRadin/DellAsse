package com.dellasse.backend.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.dellasse.backend.models.Gallery;

/**
 * Repositório para a entidade Gallery.
 * <p>
 * Fornece métodos para operações de CRUD e consultas relacionadas às galerias de imagens.
 */
public interface GalleryRepository extends JpaRepository<Gallery, Long>{

    @Query("select g from Gallery g")
    List<Gallery> findAllWithRelations();
}
