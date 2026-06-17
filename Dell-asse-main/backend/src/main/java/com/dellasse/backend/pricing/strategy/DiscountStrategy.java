package com.dellasse.backend.pricing.strategy;

import com.dellasse.backend.pricing.PartyBudgetContext;

/**
 * Strategy para aplicar descontos sobre o orçamento de uma festa.
 * <p>
 * Cada implementação encapsula uma regra de negócio diferente
 * (volume de produtos, categoria, sazonalidade, etc.).
 */
public interface DiscountStrategy {

    /**
     * Indica se a estratégia se aplica ao contexto informado.
     */
    boolean supports(PartyBudgetContext context);

    /**
     * Calcula o valor do desconto (sempre positivo) sobre o subtotal.
     */
    double calculateDiscount(PartyBudgetContext context);

    /**
     * Nome legível da estratégia, usado no detalhamento do orçamento.
     */
    String getName();
}
