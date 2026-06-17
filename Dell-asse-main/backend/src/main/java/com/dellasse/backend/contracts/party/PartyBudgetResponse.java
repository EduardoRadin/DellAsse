package com.dellasse.backend.contracts.party;

import java.util.List;

/**
 * DTO com o detalhamento do orçamento calculado pelo back-end.
 */
public record PartyBudgetResponse(
    double subtotal,
    double discountAmount,
    double total,
    List<DiscountDetail> discounts
) {
    public record DiscountDetail(String name, double amount) {}
}
