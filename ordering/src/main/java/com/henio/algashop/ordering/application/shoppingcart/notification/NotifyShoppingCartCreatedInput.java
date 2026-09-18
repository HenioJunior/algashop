package com.henio.algashop.ordering.application.shoppingcart.notification;

import java.time.LocalDateTime;

public record NotifyShoppingCartCreatedInput(
        String shoppingCartId,
        String customerId,
        LocalDateTime createdAt
) {}
