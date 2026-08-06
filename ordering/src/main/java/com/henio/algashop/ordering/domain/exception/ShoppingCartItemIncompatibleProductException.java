package com.henio.algashop.ordering.domain.exception;

import com.henio.algashop.ordering.domain.valueobject.id.ProductId;
import com.henio.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;

import static com.henio.algashop.ordering.domain.exception.CustomerMessages.ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT;

public class ShoppingCartItemIncompatibleProductException extends DomainException {
    public ShoppingCartItemIncompatibleProductException(ShoppingCartItemId id, ProductId productId) {
        super(String.format(ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT, id, productId));
    }
}
