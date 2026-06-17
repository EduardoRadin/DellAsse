package com.dellasse.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dellasse.backend.contracts.product.ProductCreateRequest;
import com.dellasse.backend.contracts.product.ProductResponse;
import com.dellasse.backend.contracts.product.ProductUpdateRequest;
import com.dellasse.backend.contracts.product.ProductUpdateResponse;
import com.dellasse.backend.service.ProductService;

import jakarta.validation.Valid;

/**
 * Controlador REST para gerenciar os Produtos.
 * <p>
 * Este controlador fornece endpoints para criar, atualizar e consultar produtos.
 *
 * @author  Dell'Asse
 * @version 1.0
 * @since 2025-11-21
 */
@RestController
@RequestMapping("/product")
public class ProductContoller {
    
    @Autowired
    private ProductService productService;

    /**
     * Cria um novo Produto.
     *
     * @param createRequest DTO contendo os dados do Produto a ser criado.
     * @param token         Token JWT do usuário autenticado.
     * @return ResponseEntity com o resultado da operação.
     */
    @PostMapping("/create")
    public ResponseEntity<?> create(@Valid @RequestBody ProductCreateRequest createRequest, JwtAuthenticationToken token){
        return productService.create(createRequest, token.getName());
    }

     /**
      * Atualiza os dados de um Produto existente.
      *
      * @param updateRequest DTO contendo os dados atualizados do Produto.
      * @param id            O ID do Produto a ser atualizado.
      * @param token         Token JWT do usuário autenticado.
      * @return DTO contendo os dados do Produto atualizado.
      */
    @PatchMapping("/update/{id}")
    public ProductUpdateResponse update(@Valid @RequestBody ProductUpdateRequest updateRequest, @PathVariable Long id, JwtAuthenticationToken token){
        return productService.update(updateRequest, id, token.getName());
    }

    /**
     * Retorna todos os produtos.
     *
     * @return Lista de DTOs contendo os dados dos produtos.
     */
    @GetMapping("/all")
    public List<ProductResponse> getAll() {
        return productService.getAll();
    }

    @GetMapping("/{id}")
    public ProductResponse getById(@PathVariable Long id, JwtAuthenticationToken token) {
        return productService.getById(id, token.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, JwtAuthenticationToken token) {
        return productService.delete(id, token.getName());
    }
}
