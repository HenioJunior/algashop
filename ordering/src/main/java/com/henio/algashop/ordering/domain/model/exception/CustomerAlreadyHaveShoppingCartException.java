package com.henio.algashop.ordering.domain.model.exception;

import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.ERROR_CUSTOMER_ALREADY_HAVE_SHOPPING_CART;

public class CustomerAlreadyHaveShoppingCartException extends DomainException {

    public CustomerAlreadyHaveShoppingCartException(CustomerId customerId) {
        super(String.format(ERROR_CUSTOMER_ALREADY_HAVE_SHOPPING_CART, customerId));
    }
}
