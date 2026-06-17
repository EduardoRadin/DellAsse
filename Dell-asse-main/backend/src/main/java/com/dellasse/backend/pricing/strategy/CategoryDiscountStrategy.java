package com.dellasse.backend.pricing.strategy;

import org.springframework.stereotype.Component;

import com.dellasse.backend.pricing.PartyBudgetContext;
import com.dellasse.backend.models.Product;

/**
 * Desconto por categoria: 10% quando há produtos da categoria "decoracao".
 */
@Component
public class CategoryDiscountStrategy implements DiscountStrategy {

    private static final String TARGET_CATEGORY = "decoracao";
    private static final double DISCOUNT_RATE = 0.10;

    @Override
    public boolean supports(PartyBudgetContext context) {
        if (context.products() == null) {
            return false;
        }
        return context.products().stream()
            .map(Product::getCategory)
            .anyMatch(category -> TARGET_CATEGORY.equalsIgnoreCase(category));
    }

    @Override
    public double calculateDiscount(PartyBudgetContext context) {
        double categorySubtotal = context.products().stream()
            .filter(p -> TARGET_CATEGORY.equalsIgnoreCase(p.getCategory()))
            .mapToDouble(p -> p.getPrice() != null ? p.getPrice() : 0.0)
            .sum();
        return categorySubtotal * DISCOUNT_RATE;
    }

    @Override
    public String getName() {
        return "Desconto em decoração (10%)";
    }
}
