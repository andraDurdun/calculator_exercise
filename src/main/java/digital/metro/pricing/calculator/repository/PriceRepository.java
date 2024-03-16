package digital.metro.pricing.calculator.repository;

import digital.metro.pricing.calculator.exception.ResourceNotFoundException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A dummy implementation for testing purposes. In production, we would get real prices from a database.
 */
@Repository
public class PriceRepository {

    private final Map<String, BigDecimal> prices = new ConcurrentHashMap<>();
    private final Map<String, BigDecimal> discounts;

    public PriceRepository() {
        this.discounts = new HashMap<>();
        discounts.put("customer-1", new BigDecimal("0.90"));
        discounts.put("customer-2", new BigDecimal("0.85"));
    }

    public BigDecimal getPriceByArticleId(String articleId) {
        return prices.computeIfAbsent(articleId, key -> calculateRandomPrice());
    }

    public BigDecimal getPriceByArticleIdAndCustomerId(String articleId, String customerId) {
        if (discounts.containsKey(customerId)) {
            return applyDiscount(getPriceByArticleId(articleId), discounts.get(customerId));
        }

        throw new ResourceNotFoundException(String.format("No price found for articleId: %s and customerId: %s.", articleId, customerId));
    }

    private BigDecimal calculateRandomPrice() {
        return BigDecimal.valueOf(0.5d + ThreadLocalRandom.current().nextDouble() * 29.50d).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal applyDiscount(BigDecimal price, BigDecimal discount) {
        return price.multiply(discount).setScale(2, RoundingMode.HALF_UP);
    }
}
