package com.henio.algashop.ordering.domain.model.shoppingcart.exception;

import com.henio.algashop.ordering.domain.model.shared.DomainException;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;

public class ShoppingCartNotFoundException extends DomainException {
    public ShoppingCartNotFoundException(ShoppingCartId shoppingCartId) {
        super("Shopping cart not found with id: " + shoppingCartId);
    }
}
