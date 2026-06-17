package com.dellasse.backend.pricing;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dellasse.backend.contracts.party.PartyBudgetResponse;
import com.dellasse.backend.contracts.party.PartyBudgetResponse.DiscountDetail;
import com.dellasse.backend.models.Product;
import com.dellasse.backend.pricing.strategy.DiscountStrategy;
import com.dellasse.backend.repositories.ProductRepository;

/**
 * Serviço que calcula o orçamento de festas aplicando a cadeia de {@link DiscountStrategy}.
 * <p>
 * Padrão Strategy: cada regra de desconto é uma estratégia intercambiável;
 * este serviço orquestra qual estratégia se aplica e consolida o resultado.
 */
@Service
public class PartyBudgetCalculator {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private List<DiscountStrategy> discountStrategies;

    public PartyBudgetResponse calculate(List<Long> productIds) {
        List<Product> products = loadProducts(productIds);
        double subtotal = products.stream()
            .mapToDouble(p -> p.getPrice() != null ? p.getPrice() : 0.0)
            .sum();

        PartyBudgetContext context = new PartyBudgetContext(products, subtotal);
        List<DiscountDetail> discounts = new ArrayList<>();
        double totalDiscount = 0.0;

        for (DiscountStrategy strategy : discountStrategies) {
            if (strategy.supports(context)) {
                double amount = strategy.calculateDiscount(context);
                if (amount > 0) {
                    discounts.add(new DiscountDetail(strategy.getName(), round(amount)));
                    totalDiscount += amount;
                }
            }
        }

        double total = Math.max(0, subtotal - totalDiscount);
        return new PartyBudgetResponse(
            round(subtotal),
            round(totalDiscount),
            round(total),
            discounts
        );
    }

    private List<Product> loadProducts(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            return List.of();
        }
        return productRepository.findAllById(productIds);
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
