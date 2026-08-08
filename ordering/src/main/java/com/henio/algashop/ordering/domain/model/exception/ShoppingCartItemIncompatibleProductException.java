package com.henio.algashop.ordering.domain.model.exception;

import com.henio.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.henio.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT;

public class ShoppingCartItemIncompatibleProductException extends DomainException {
    public ShoppingCartItemIncompatibleProductException(ShoppingCartItemId id, ProductId productId) {
        super(String.format(ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT, id, productId));
    }
}
