package com.dellasse.backend.contracts.enterprise;

import jakarta.validation.constraints.NotBlank;

/**
 * Representa o objeto de transferência de dados (DTO) para a criação de uma nova Empresa.
 * <p>
 * Este record encapsula os dados necessários que são enviados pela API
 * para registrar uma nova empresa no sistema.
 *
 * @param name        O nome oficial da empresa. Não pode estar em branco.
 * @param document    O documento de identificação da empresa (ex: CNPJ). Não pode estar em branco.
 * @param address     O endereço físico da empresa. Não pode estar em branco.
 * @param phoneNumber O número de telefone principal de contato. Não pode estar em branco.
 * @param email       O endereço de e-mail principal de contato. Não pode estar em branco.
 * @param urlImage    Uma URL opcional para a imagem (logo) da empresa.
 */
public record EnterpriseCreateRequest(

    @NotBlank(message = "Name is required") 
    String name, 
    
    @NotBlank(message = "Document is required")
    String document,

    @NotBlank(message = "Address is required") 
    String address, 

    @NotBlank(message = "Phone number is required")
    String phoneNumber, 
    
    @NotBlank(message = "Email is required")
    String email, 

    String urlImage
) {}
