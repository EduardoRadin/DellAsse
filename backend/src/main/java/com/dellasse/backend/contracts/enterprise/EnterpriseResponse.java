package com.dellasse.backend.contracts.enterprise;

import java.util.UUID;

/**
 * Representa o objeto de transferência de dados (DTO) para a resposta da Empresa.
 * <p>
 * Este record encapsula os dados necessários que são enviados pela API
 * para recuperar os dados de uma empresa.
 *
 * @param id          O id da empresa.
 * @param name        O nome oficial da empresa.
 * @param document    O documento de identificação da empresa (ex: CNPJ).
 * @param address     O endereço físico da empresa.
 * @param phoneNumber O número de telefone principal de contato.
 * @param email       O endereço de e-mail principal de contato.
 * @param urlImage    Uma URL opcional para a imagem (logo) da empresa.
 */

public record EnterpriseResponse(
    UUID id,
    String name,
    String document,
    String address,
    String phoneNumber,
    String email,
    String urlImage
) {}
