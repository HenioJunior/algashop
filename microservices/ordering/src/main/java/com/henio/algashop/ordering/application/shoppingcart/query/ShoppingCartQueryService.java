package com.henio.algashop.ordering.application.shoppingcart.query;

import java.util.UUID;

public interface ShoppingCartQueryService {
    ShoppingCartOutput findById(String shoppingCartId);
    ShoppingCartOutput findByCustomerId(UUID customerId);

}
