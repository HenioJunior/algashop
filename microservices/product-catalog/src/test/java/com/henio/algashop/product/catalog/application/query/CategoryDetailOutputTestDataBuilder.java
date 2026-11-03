package com.henio.algashop.product.catalog.application.query;

import com.henio.algashop.product.catalog.application.category.query.CategoryDetailOutput;

import java.util.UUID;

public class CategoryDetailOutputTestDataBuilder {

    private CategoryDetailOutputTestDataBuilder() {
    }

    public static CategoryDetailOutput.CategoryDetailOutputBuilder aCategory() {
        return CategoryDetailOutput.builder()
                .id(UUID.randomUUID())
                .name("Notebook")
                .enabled(true);
    }

    public static CategoryDetailOutput.CategoryDetailOutputBuilder aCategoryAlt() {
        return CategoryDetailOutput.builder()
                .id(UUID.randomUUID())
                .name("Desktop")
                .enabled(true);
    }
}
