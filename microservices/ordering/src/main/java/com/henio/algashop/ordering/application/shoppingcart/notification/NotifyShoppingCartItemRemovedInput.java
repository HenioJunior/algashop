package com.henio.algashop.ordering.application.shoppingcart.notification;

import java.time.OffsetDateTime;

public record NotifyShoppingCartItemRemovedInput(
        String shoppingCartId,
        String customerId,
        String productId,
        OffsetDateTime removedAt
) {}
