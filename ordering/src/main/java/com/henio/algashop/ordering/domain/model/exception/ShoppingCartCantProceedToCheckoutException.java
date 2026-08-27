package com.henio.algashop.ordering.domain.model.exception;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.ERROR_SHOPPING_CART_IS_NOT_VALID;

public class ShoppingCartCantProceedToCheckoutException extends DomainException {
    public ShoppingCartCantProceedToCheckoutException() {
        super(ERROR_SHOPPING_CART_IS_NOT_VALID);
    }
}
