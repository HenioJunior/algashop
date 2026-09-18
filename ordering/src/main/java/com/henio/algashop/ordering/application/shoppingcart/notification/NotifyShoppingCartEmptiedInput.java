package com.henio.algashop.ordering.application.shoppingcart.notification;

import java.time.OffsetDateTime;

public record NotifyShoppingCartEmptiedInput(
        String shoppingCartId,
        String customerId,
        OffsetDateTime emptiedAt
) {}
