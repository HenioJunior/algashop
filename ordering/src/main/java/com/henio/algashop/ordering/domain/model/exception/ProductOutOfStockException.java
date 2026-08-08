package com.henio.algashop.ordering.domain.model.exception;

import com.henio.algashop.ordering.domain.model.valueobject.id.ProductId;

import static com.henio.algashop.ordering.domain.model.exception.CustomerMessages.ERROR_PRODUCT_IS_OUT_OF_STOCK;

public class ProductOutOfStockException extends DomainException {
    public ProductOutOfStockException(ProductId id) {
        super(String.format(ERROR_PRODUCT_IS_OUT_OF_STOCK, id));
    }
}
