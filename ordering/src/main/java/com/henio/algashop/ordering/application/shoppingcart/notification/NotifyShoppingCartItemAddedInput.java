package com.henio.algashop.ordering.application.shoppingcart.notification;

import java.time.LocalDateTime;

public record NotifyShoppingCartItemAddedInput(
        String shoppingCartId,
        String customerId,
        String productId,
        LocalDateTime addedAt
) {}
