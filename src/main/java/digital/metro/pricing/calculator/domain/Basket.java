package digital.metro.pricing.calculator.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Set;

public class Basket {

    @NotBlank
    private final String customerId;
    @Valid
    private final Set<BasketEntry> entries;

    public Basket(String customerId, Set<BasketEntry> entries) {
        this.customerId = customerId;
        this.entries = entries;
    }

    public String getCustomerId() {
        return customerId;
    }

    public Set<BasketEntry> getEntries() {
        return entries;
    }
}
