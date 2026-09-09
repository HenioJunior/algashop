package com.henio.algashop.ordering.domain.model.shoppingcart;

import com.henio.algashop.ordering.domain.model.shared.DomainException;
import com.henio.algashop.ordering.domain.model.product.ProductId;

import static com.henio.algashop.ordering.domain.model.validation.ErrorMessages.ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT;

public class ShoppingCartItemIncompatibleProductException extends DomainException {
    public ShoppingCartItemIncompatibleProductException(ShoppingCartItemId id, ProductId productId) {
        super(String.format(ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT, id, productId));
    }
}
