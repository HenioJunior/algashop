package com.henio.algashop.ordering.domain.exception;

import com.henio.algashop.ordering.domain.valueobject.id.ProductId;
import com.henio.algashop.ordering.domain.valueobject.id.ShoppingCartId;

import static com.henio.algashop.ordering.domain.exception.CustomerMessages.ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT;

public class ShoppingCartDoesNotContainProductException extends DomainException {
    public ShoppingCartDoesNotContainProductException(ShoppingCartId id, ProductId productId) {
        super(String.format(ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT, id, productId));
    }
}
