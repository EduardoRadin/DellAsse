package com.dellasse.backend.contracts.enterprise;

/**
 * Representa o objeto de transferência de dados (DTO) para a criação de uma nova Empresa.
 * <p>
 * Este record encapsula os dados necessários que são enviados pela API
 * para atualizar uma empresa no sistema.
 *
 * @param name        O nome oficial da empresa. Não pode estar em branco.
 * @param document    O documento de identificação da empresa (ex: CNPJ). Não pode estar em branco.
 * @param address     O endereço físico da empresa. Não pode estar em branco.
 * @param phoneNumber O número de telefone principal de contato. Não pode estar em branco.
 * @param email       O endereço de e-mail principal de contato. Não pode estar em branco.
 * @param urlImage    Uma URL opcional para a imagem (logo) da empresa.
 */
public record EnterpriseUpdateRequest(

    String name,
    String document,
    String address,
    String phoneNumber,
    String email,
    String urlImage
    
) {}