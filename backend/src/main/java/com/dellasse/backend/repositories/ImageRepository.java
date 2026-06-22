package com.dellasse.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dellasse.backend.models.Image;

/**
 * Repositório para a entidade Image.
 * <p>
 * Fornece métodos para operações de CRUD e consultas relacionadas às imagens.
 */
public interface ImageRepository extends JpaRepository<Image, Long>{
    
}
