package com.henio.algashop.ordering.infrastructure.product.client.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ProductResponse {
    private UUID id;
    private String name;
    private BigDecimal salePrice;
    private Boolean inStock;
}
