package com.henio.algashop.ordering.application.shoppingcart.management;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartItemInput {

    private UUID shoppingCartId;

    @NotNull
    private UUID productId;

    @Positive
    @NotNull
    private Integer quantity;
}
