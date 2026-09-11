package com.henio.algashop.ordering.domain.model.product;

import com.henio.algashop.ordering.domain.model.shared.DomainException;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(ProductId id) {
        super(String.format("Product with id %s not found", id));
    }
}
