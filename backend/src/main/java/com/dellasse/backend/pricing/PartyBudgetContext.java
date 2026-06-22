package com.dellasse.backend.pricing;

import java.util.List;

import com.dellasse.backend.models.Product;

/**
 * Contexto imutável com os dados necessários para calcular o orçamento de uma festa.
 */
public record PartyBudgetContext(
    List<Product> products,
    double subtotal
) {
    public int productCount() {
        return products == null ? 0 : products.size();
    }
}
