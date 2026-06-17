package com.dellasse.backend.contracts.party;

import java.util.List;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para pré-visualização do orçamento antes de criar a festa.
 */
public record PartyBudgetPreviewRequest(
    @NotNull(message = "A lista de produtos é obrigatória")
    List<Long> products
) {}
