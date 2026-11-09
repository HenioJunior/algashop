package com.henio.algashop.ordering.domain.model.shoppingcart.exception;

import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.shared.DomainEntityNotFoundException;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;

public class ShoppingCartNotFoundException extends DomainEntityNotFoundException {
    public ShoppingCartNotFoundException(ShoppingCartId shoppingCartId) {
        super("Shopping cart with id %s not found"
                .formatted(shoppingCartId.value()));
    }

    public ShoppingCartNotFoundException(CustomerId customerId) {
        super("Shopping cart with id %s not found"
                .formatted(customerId.value()));
    }
}
