package com.henio.algashop.ordering.domain.model.shoppingcart.exception;

import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.shared.DomainEntityNotFoundException;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;

public class ShoppingCartNotFoundException extends DomainEntityNotFoundException {
    public ShoppingCartNotFoundException(ShoppingCartId shoppingCartId) {
        super("Shopping cart not found with id: " + shoppingCartId);
    }

    public ShoppingCartNotFoundException(CustomerId customerId) {
        super("Shopping cart not found for customer: " + customerId);
    }
}
