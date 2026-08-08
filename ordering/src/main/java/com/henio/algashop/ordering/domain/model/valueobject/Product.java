package com.henio.algashop.ordering.domain.model.valueobject;

import com.henio.algashop.ordering.domain.model.exception.ProductOutOfStockException;
import com.henio.algashop.ordering.domain.model.valueobject.id.ProductId;
import lombok.Builder;

import java.util.Objects;

@Builder
public record Product(
        ProductId id,
        ProductName name,
        Money price,
        boolean inStock
) {
    public Product {
        Objects.requireNonNull(id, "Product id is required");
        Objects.requireNonNull(name, "Product name is required");
        Objects.requireNonNull(price, "Product price is required");
    }

    public void checkOutOfStock() {
        if (isOutOfStock()) {
            throw new ProductOutOfStockException(this.id());
        }
    }

    private boolean isOutOfStock() {
        return !inStock();
    }
}
