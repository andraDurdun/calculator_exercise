package digital.metro.pricing.calculator.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class BasketEntry {

    @NotBlank
    private final String articleId;
    @Positive
    private final BigDecimal quantity;

    public BasketEntry(String articleId, BigDecimal quantity) {
        this.articleId = articleId;
        this.quantity = quantity;
    }

    public String getArticleId() {
        return articleId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }
}
