package com.henio.algashop.product.catalog.application.product.management;

import java.util.UUID;

public interface ProductManagementApplicationService {
    UUID create(ProductInput productInput);
    void update(UUID productId, ProductInput productInput);
    void disable(UUID productId);
}
