package digital.metro.pricing.calculator.service;

import digital.metro.pricing.calculator.domain.Basket;
import digital.metro.pricing.calculator.domain.BasketCalculationResult;
import digital.metro.pricing.calculator.domain.BasketEntry;
import digital.metro.pricing.calculator.repository.PriceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BasketCalculatorService {

    private final PriceRepository priceRepository;

    public BasketCalculatorService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public BasketCalculationResult calculateBasket(Basket basket) {
        Map<String, BigDecimal> pricedArticles = basket.getEntries().stream()
                .collect(Collectors.toMap(
                        BasketEntry::getArticleId,
                        entry -> calculateArticle(entry, basket.getCustomerId()).multiply(entry.getQuantity())));

        BigDecimal totalAmount = pricedArticles.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new BasketCalculationResult(basket.getCustomerId(), pricedArticles, totalAmount);
    }

    public BigDecimal calculateArticle(BasketEntry basketEntry) {
        return priceRepository.getPriceByArticleId(basketEntry.getArticleId());
    }

    public BigDecimal calculateArticle(BasketEntry basketEntry, String customerId) {
        return priceRepository.getPriceByArticleIdAndCustomerId(basketEntry.getArticleId(), customerId);
    }

}
