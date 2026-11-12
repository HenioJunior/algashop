package com.henio.algashop.ordering.presentation.shoppingCart;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShoppingCartInput {
    @NotNull
    private String customerId;
}
