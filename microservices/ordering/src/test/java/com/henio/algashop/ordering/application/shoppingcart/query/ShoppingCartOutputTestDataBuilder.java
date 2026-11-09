package com.henio.algashop.ordering.application.shoppingcart.query;

import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.product.ProductId;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartItemId;

import java.math.BigDecimal;
import java.util.List;

public class ShoppingCartOutputTestDataBuilder {
    public static ShoppingCartOutput.ShoppingCartOutputBuilder aShoppingCart() {
        return ShoppingCartOutput.builder()
                .id(new ShoppingCartId().value().toString())
                .customerId(new CustomerId().value().toString())
                .totalItems(3)
                .totalAmount(new BigDecimal(1250))
                .items(List.of(
                        existingItem().build(),
                        existingItemAlt().build()
                ));
    }

    public static ShoppingCartItemOutput.ShoppingCartItemOutputBuilder existingItem() {
        return ShoppingCartItemOutput.builder()
                .id(new ShoppingCartItemId().value().toString())
                .productId(new ProductId().value().toString())
                .price(new BigDecimal(500))
                .quantity(2)
                .totalAmount(new BigDecimal(1000))
                .available(true)
                .name("Notebook");
    }

    public static ShoppingCartItemOutput.ShoppingCartItemOutputBuilder existingItemAlt() {
        return ShoppingCartItemOutput.builder()
                .id(new ShoppingCartId().value().toString())
                .productId(new ProductId().value().toString())
                .price(new BigDecimal(250))
                .quantity(1)
                .totalAmount(new BigDecimal(250))
                .available(true)
                .name("Mouse pad");
    }
}
