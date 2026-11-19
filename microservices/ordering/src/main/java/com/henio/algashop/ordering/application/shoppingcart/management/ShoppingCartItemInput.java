package com.henio.algashop.ordering.application.shoppingcart.management;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartItemInput {
    public UUID shoppingCartId;
    private UUID productId;
    private int quantity;
}
