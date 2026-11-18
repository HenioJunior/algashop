package com.henio.algashop.ordering.infrastructure.product.client.http;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface ProductCatalogAPIClient {
    @GetExchange(value = "/api/v1/products/{productId}", accept = "application/json")
    ProductResponse getById(@PathVariable String productId);
}
