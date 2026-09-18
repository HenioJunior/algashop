package com.henio.algashop.ordering.domain.model.shoppingcart.exception;

import com.henio.algashop.ordering.domain.model.shared.DomainException;
import com.henio.algashop.ordering.domain.model.product.ProductId;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartId;

import static com.henio.algashop.ordering.domain.model.validation.ErrorMessages.ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT;

public class ShoppingCartDoesNotContainProductException extends DomainException {
    public ShoppingCartDoesNotContainProductException(ShoppingCartId id, ProductId productId) {
        super(String.format(ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT, id, productId));
    }
}
