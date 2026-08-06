package com.henio.algashop.ordering.domain.exception;

import com.henio.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import com.henio.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;

import static com.henio.algashop.ordering.domain.exception.CustomerMessages.ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_ITEM;

public class ShoppingCartDoesNotContainItemException extends DomainException {
    public ShoppingCartDoesNotContainItemException(ShoppingCartId id, ShoppingCartItemId shoppingCartItemId) {
        super(String.format(ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_ITEM, id, shoppingCartItemId));
    }
}
