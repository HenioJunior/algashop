package com.henio.algashop.product.catalog.application.category.query;

import com.henio.algashop.product.catalog.application.product.query.PageModel;

import java.util.UUID;

public interface CategoryQueryService {
    CategoryDetailOutput findById(UUID categoryId);
    PageModel<CategoryDetailOutput> filter(Integer size, Integer number);
}
