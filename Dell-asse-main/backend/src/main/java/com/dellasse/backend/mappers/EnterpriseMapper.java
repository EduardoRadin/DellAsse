package com.dellasse.backend.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.dellasse.backend.contracts.enterprise.EnterpriseCreateRequest;
import com.dellasse.backend.contracts.enterprise.EnterpriseResponse;
import com.dellasse.backend.contracts.enterprise.EnterpriseUpdateRequest;
import com.dellasse.backend.models.Enterprise;

/**
 * Classe responsável por mapear entre entidades Enterprise e seus respectivos DTOs.
 * <p>
 * Fornece métodos para converter entre EnterpriseCreateRequest, EnterpriseUpdateRequest,
 * EnterpriseResponse e a entidade Enterprise.
 */
public class EnterpriseMapper {
    
    /** 
     * Converte um DTO de criação de empresa para a entidade Enterprise.
     *
     * @param dtoRequest O DTO contendo os dados para criar uma nova empresa.
     * @return A entidade Enterprise criada a partir do DTO.
     */
    public static Enterprise toEntity(EnterpriseCreateRequest dtoRequest) {
        return new Enterprise(
            dtoRequest.name(),
            dtoRequest.address(),
            dtoRequest.phoneNumber(),
            dtoRequest.email(),
            dtoRequest.urlImage()
        );
    }

    /** 
     * Converte um DTO de atualização de empresa para a entidade Enterprise.
     *
     * @param dtoRequest O DTO contendo os dados para atualizar uma empresa existente.
     * @return A entidade Enterprise atualizada a partir do DTO.
     */
    public static Enterprise toEntityUpdate(EnterpriseUpdateRequest dtoRequest){
        return new Enterprise(
            dtoRequest.name(),
            dtoRequest.document(),
            dtoRequest.address(),
            dtoRequest.phoneNumber(),
            dtoRequest.email(),
            dtoRequest.urlImage()
        );
    }

    /** 
     * Converte uma entidade Enterprise para o DTO de resposta.
     *
     * @param enterprise A entidade Enterprise a ser convertida.
     * @return O DTO de resposta contendo os dados da empresa.
     */
    public static EnterpriseResponse toEnterpriseResponse(Enterprise enterprise){
        return new EnterpriseResponse(
            enterprise.getId(),
            enterprise.getName(),
            enterprise.getDocument(),
            enterprise.getAddress(),
            enterprise.getPhoneNumber(),
            enterprise.getEmail(),
            enterprise.getUrlImage()
        );
    }

    /** 
     * Converte uma lista de entidades Enterprise para uma lista de DTOs de resposta.
     *
     * @param enterprises A lista de entidades Enterprise a ser convertida.
     * @return A lista de DTOs de resposta contendo os dados das empresas.
     */
    public static List<EnterpriseResponse> toListEnterpriseResponse(List<Enterprise> enterprises){
        return enterprises.stream()
                .map(EnterpriseMapper::toEnterpriseResponse)
                .collect(Collectors.toList());
    }
}
