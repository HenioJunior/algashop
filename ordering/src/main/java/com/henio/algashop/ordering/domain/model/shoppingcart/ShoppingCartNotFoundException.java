package com.henio.algashop.ordering.domain.model.shoppingcart;

import com.henio.algashop.ordering.domain.model.shared.DomainException;

public class ShoppingCartNotFoundException extends DomainException {
    public ShoppingCartNotFoundException(ShoppingCartId shoppingCartId) {
        super("Shopping cart not found with id: " + shoppingCartId);
    }
}
