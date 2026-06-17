package com.dellasse.backend.mappers;

import java.util.List;
import java.util.stream.Collectors;

import com.dellasse.backend.contracts.party.PartyCreateRequest;
import com.dellasse.backend.contracts.party.PartyResponse;
import com.dellasse.backend.contracts.party.PartyUpdateRequest;
import com.dellasse.backend.models.Gallery;
import com.dellasse.backend.models.Party;

/**
 * Classe responsável por mapear entre entidades Party e seus respectivos DTOs.
 * <p>
 * Fornece métodos para converter entre PartyCreateRequest, PartyResponse e a entidade Party.
 */
public class PartyMapper {
    
    /** * Converte um DTO de criação de festa para a entidade Party.
     *
     * @param create O DTO contendo os dados para criar uma nova festa.
     * @return A entidade Party criada a partir do DTO.
     */
    public static Party toEntity(PartyCreateRequest create){
        Gallery gallery = null;
        if (create.galleryId() != null) {
            gallery = new Gallery(create.galleryId());
        }
        
        return new Party(
            create.title(),
            create.description(),
            create.products(),
            create.generateBudget(),
            create.observations(),
            create.imageURL(),
            gallery
        );
    }

    /** * Converte uma entidade Party para o DTO de resposta.
     *
     * @param entity A entidade Party a ser convertida.
     * @return O DTO de resposta contendo os dados da festa.
     */
    public static PartyResponse toResponse(Party entity) {
        return new PartyResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getObservations(),
                entity.getGenerateBudget(),
                entity.getImgExample(),
                entity.getStatus(),
                
                // Nome do Usuário
                entity.getUser() != null ? entity.getUser().getName() : "Usuário Desconhecido",
                
                // --- NOVO CAMPO ADICIONADO: Telefone do Usuário ---
                entity.getUser() != null ? entity.getUser().getPhone() : null,
                // --------------------------------------------------

                entity.getLastAtualization(),
                entity.getProducts().stream()
                    .map(ProductMapper::toResponse)
                    .collect(Collectors.toList())
        );
    }
    
    /** * Converte uma lista de entidades Party para uma lista de DTOs de resposta.
     *
     * @param entity A lista de entidades Party a ser convertida.
     * @return A lista de DTOs de resposta contendo os dados das festas.
     */
    public static List<PartyResponse> toResponse(List<Party> entity){
        return entity.stream()
                .map(PartyMapper::toResponse)
                .collect(Collectors.toList());
    }

    /** * Atualiza uma entidade Party existente com os dados de um DTO de atualização.
     *
     * @param entity A entidade Party a ser atualizada.
     * @param update O DTO contendo os dados atualizados da festa.
     */
    public static void updateEntity(Party entity, PartyCreateRequest update){
        entity.setTitle(update.title());
        entity.setDescription(update.description());
        entity.setGenerateBudget(update.generateBudget());
        entity.setObservations(update.observations());
        entity.setImgExample(update.imageURL());
    }

    /** * Atualiza uma entidade Party existente com os dados de um DTO de atualização.
     *
     * @param entity A entidade Party a ser atualizada.
     * @param update O DTO contendo os dados atualizados da festa.
     */
    public static void updateEntity(Party entity, PartyUpdateRequest update){
        if (update.title() != null) entity.setTitle(update.title());
        if (update.description() != null) entity.setDescription(update.description());
        if (update.generateBudget() != null) entity.setGenerateBudget(update.generateBudget());
        if (update.observations() != null) entity.setObservations(update.observations());
        if (update.imageURL() != null) entity.setImgExample(update.imageURL());
    }
}