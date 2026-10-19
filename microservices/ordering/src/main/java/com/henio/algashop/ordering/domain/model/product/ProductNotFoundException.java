package com.henio.algashop.ordering.domain.model.product;

import com.henio.algashop.ordering.domain.model.shared.DomainEntityNotFoundException;

public class ProductNotFoundException extends DomainEntityNotFoundException {
    public ProductNotFoundException(ProductId id) {
        super(String.format("Product with id %s not found", id));
    }
}
