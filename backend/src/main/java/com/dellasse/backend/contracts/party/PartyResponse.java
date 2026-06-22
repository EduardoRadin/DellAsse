package com.dellasse.backend.contracts.party;

import java.time.LocalDateTime;
import java.util.List;

import com.dellasse.backend.contracts.product.ProductResponse;

/**
 * Representa o objeto de transferência de dados (DTO) para a resposta de uma party.
 * * @param id             O id da party.
 * @param title          O título da party.
 * @param observations   As observações da party (Renomeado para plural).
 * @param generateBudget O orçamento como número (Double).
 * @param imageUrl       A URL da imagem associada à party.
 * @param status         O status atual da party.
 * @param userName       Nome do usuário que solicitou (Novo).
 * @param userPhone      Telefone do usuário que solicitou (Novo).
 * @param requestDate    Data da solicitação/atualização (Novo).
 * @param products       Lista de produtos associados à party.
 */
public record PartyResponse(

    Long id,
    String title,
    String observations,       
    Double generateBudget,     
    String imageUrl,
    String status,
    String userName,  
    String userPhone,         
    LocalDateTime requestDate, 
    List<ProductResponse> products

) {}