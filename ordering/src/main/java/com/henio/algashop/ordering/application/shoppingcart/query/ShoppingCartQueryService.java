package com.henio.algashop.ordering.application.shoppingcart.query;

public interface ShoppingCartQueryService {
    ShoppingCartOutput findById(String shoppingCartId);
    ShoppingCartOutput findByCustomerId(String customerId);

}
