package com.henio.algashop.ordering.domain.model.shoppingcart;

import com.henio.algashop.ordering.domain.model.DomainException;

import static com.henio.algashop.ordering.domain.model.ErrorMessages.ERROR_SHOPPING_CART_IS_NOT_VALID;

public class ShoppingCartCantProceedToCheckoutException extends DomainException {
    public ShoppingCartCantProceedToCheckoutException() {
        super(ERROR_SHOPPING_CART_IS_NOT_VALID);
    }
}
