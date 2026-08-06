package com.henio.algashop.ordering.domain.exception;

import com.henio.algashop.ordering.domain.valueobject.id.ProductId;

import static com.henio.algashop.ordering.domain.exception.CustomerMessages.ERROR_PRODUCT_IS_OUT_OF_STOCK;

public class ProductOutOfStockException extends DomainException {
    public ProductOutOfStockException(ProductId id) {
        super(String.format(ERROR_PRODUCT_IS_OUT_OF_STOCK, id));
    }
}
