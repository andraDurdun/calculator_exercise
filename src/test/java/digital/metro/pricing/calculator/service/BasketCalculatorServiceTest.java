package digital.metro.pricing.calculator.service;

import digital.metro.pricing.calculator.domain.Basket;
import digital.metro.pricing.calculator.domain.BasketCalculationResult;
import digital.metro.pricing.calculator.domain.BasketEntry;
import digital.metro.pricing.calculator.repository.PriceRepository;
import digital.metro.pricing.calculator.service.BasketCalculatorService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BasketCalculatorServiceTest {

    @Mock
    private PriceRepository mockPriceRepository;

    private BasketCalculatorService service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        service = new BasketCalculatorService(mockPriceRepository);
    }

    @Test
    public void testCalculateArticle() {
        // GIVEN
        String articleId = "article-1";
        BigDecimal price = new BigDecimal("34.29");
        Mockito.when(mockPriceRepository.getPriceByArticleId(articleId)).thenReturn(price);

        // WHEN
        BigDecimal result = service.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE));

        // THEN
        Assertions.assertThat(result).isEqualByComparingTo(price);
    }

    @Test
    public void testCalculateArticleForCustomer() {
        // GIVEN
        String articleId = "article-1";
        BigDecimal customerPrice = new BigDecimal("29.99");
        Mockito.when(mockPriceRepository.getPriceByArticleIdAndCustomerId(articleId, "customer-1")).thenReturn(customerPrice);

        // WHEN
        BigDecimal result = service.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), "customer-1");

        // THEN
        Assertions.assertThat(result).isEqualByComparingTo(customerPrice);
    }

    @Test
    public void testCalculateBasket() {
        // GIVEN
        Basket basket = new Basket("customer-1", Set.of(
                new BasketEntry("article-1", BigDecimal.TEN),
                new BasketEntry("article-2", BigDecimal.ONE),
                new BasketEntry("article-3", BigDecimal.ONE)));

        Map<String, BigDecimal> prices = Map.of(
                "article-1", new BigDecimal("1.50"),
                "article-2", new BigDecimal("0.29"),
                "article-3", new BigDecimal("9.99"));

        Mockito.when(mockPriceRepository.getPriceByArticleIdAndCustomerId("article-1", "customer-1")).thenReturn(prices.get("article-1"));
        Mockito.when(mockPriceRepository.getPriceByArticleIdAndCustomerId("article-2", "customer-1")).thenReturn(prices.get("article-2"));
        Mockito.when(mockPriceRepository.getPriceByArticleIdAndCustomerId("article-3", "customer-1")).thenReturn(prices.get("article-3"));

        // WHEN
        BasketCalculationResult result = service.calculateBasket(basket);

        // THEN
        Map<String, BigDecimal> expectedPrices = new HashMap<>();
        basket.getEntries().forEach(entry -> {
            BigDecimal expectedPrice = prices.get(entry.getArticleId()).multiply(entry.getQuantity());
            expectedPrices.put(entry.getArticleId(), expectedPrice);
        });

        BigDecimal expectedTotal = expectedPrices.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);

        Assertions.assertThat(result.getCustomerId()).isEqualTo("customer-1");
        Assertions.assertThat(result.getPricedBasketEntries()).isEqualTo(expectedPrices);
        Assertions.assertThat(result.getTotalAmount()).isEqualByComparingTo(expectedTotal);
    }
}
