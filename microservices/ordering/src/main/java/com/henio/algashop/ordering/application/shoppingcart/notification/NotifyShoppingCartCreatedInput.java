package com.henio.algashop.ordering.application.shoppingcart.notification;

import java.time.OffsetDateTime;

public record NotifyShoppingCartCreatedInput(
        String shoppingCartId,
        String customerId,
        OffsetDateTime createdAt
) {}
