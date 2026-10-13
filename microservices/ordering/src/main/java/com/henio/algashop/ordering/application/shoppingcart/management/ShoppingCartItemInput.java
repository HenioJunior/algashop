package com.henio.algashop.ordering.application.shoppingcart.management;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartItemInput {
    public String shoppingCartId;
    private String productId;
    private int quantity;
}
