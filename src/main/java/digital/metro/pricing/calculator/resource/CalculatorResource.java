package digital.metro.pricing.calculator.resource;

import digital.metro.pricing.calculator.domain.Basket;
import digital.metro.pricing.calculator.domain.BasketCalculationResult;
import digital.metro.pricing.calculator.service.BasketCalculatorService;
import digital.metro.pricing.calculator.domain.BasketEntry;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@RestController
@RequestMapping("/calculator")
public class CalculatorResource {

    private final BasketCalculatorService basketCalculatorService;

    public CalculatorResource(BasketCalculatorService basketCalculatorService) {
        this.basketCalculatorService = basketCalculatorService;
    }

    @PostMapping("/calculate-basket")
    public BasketCalculationResult calculateBasket(@RequestBody @Valid Basket basket) {
        return basketCalculatorService.calculateBasket(basket);
    }

    @GetMapping("/article/{articleId}")
    public BigDecimal getArticlePrice(@PathVariable @NotBlank String articleId) {
        return basketCalculatorService.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE));
    }

    @GetMapping("/article/{articleId}/customer/{customerId}")
    public BigDecimal getArticlePriceForCustomer(@PathVariable @NotBlank String articleId, @PathVariable @NotBlank String customerId) {
        return basketCalculatorService.calculateArticle(new BasketEntry(articleId, BigDecimal.ONE), customerId);
    }
}
