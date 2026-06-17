package com.dellasse.backend.pricing.strategy;

import org.springframework.stereotype.Component;

import com.dellasse.backend.pricing.PartyBudgetContext;

/**
 * Desconto por volume: 5% quando a festa possui 3 ou mais produtos.
 */
@Component
public class VolumeDiscountStrategy implements DiscountStrategy {

    private static final int MIN_PRODUCTS = 3;
    private static final double DISCOUNT_RATE = 0.05;

    @Override
    public boolean supports(PartyBudgetContext context) {
        return context.productCount() >= MIN_PRODUCTS;
    }

    @Override
    public double calculateDiscount(PartyBudgetContext context) {
        return context.subtotal() * DISCOUNT_RATE;
    }

    @Override
    public String getName() {
        return "Desconto por volume (5%)";
    }
}
