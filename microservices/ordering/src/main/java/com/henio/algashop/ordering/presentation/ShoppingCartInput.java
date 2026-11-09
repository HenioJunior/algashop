package com.henio.algashop.ordering.presentation;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShoppingCartInput {
    @NotNull
    private String customerId;
}
