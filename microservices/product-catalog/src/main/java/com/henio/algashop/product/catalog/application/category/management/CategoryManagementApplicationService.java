package com.henio.algashop.product.catalog.application.category.management;

import java.util.UUID;

public interface CategoryManagementApplicationService {
    UUID create(CategoryInput categoryInput);
    void update(UUID categoryId, CategoryInput categoryInput);
    void disable(UUID categoryId);
}
