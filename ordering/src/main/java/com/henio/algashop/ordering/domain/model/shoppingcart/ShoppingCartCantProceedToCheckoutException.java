package com.henio.algashop.ordering.domain.model.shoppingcart;

import com.henio.algashop.ordering.domain.model.shared.DomainException;

import static com.henio.algashop.ordering.domain.model.validation.ErrorMessages.ERROR_SHOPPING_CART_IS_NOT_VALID;

public class ShoppingCartCantProceedToCheckoutException extends DomainException {
    public ShoppingCartCantProceedToCheckoutException() {
        super(ERROR_SHOPPING_CART_IS_NOT_VALID);
    }
}
