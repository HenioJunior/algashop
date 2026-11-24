package com.henio.algashop.product.catalog.application.category.query;

import java.util.List;
import java.util.UUID;

public interface CategoryQueryService {
    CategoryDetailOutput findById(UUID categoryId);
    List<CategoryDetailOutput> findAll();
}
