package com.henio.algashop.product.catalog.infrastructure.persistence;

import com.henio.algashop.product.catalog.application.ResourceNotFoundException;
import com.henio.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.henio.algashop.product.catalog.application.category.query.CategoryQueryService;
import com.henio.algashop.product.catalog.domain.model.Category;
import com.henio.algashop.product.catalog.infrastructure.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryQueryServiceImpl implements CategoryQueryService {

    private final CategoryRepository categoryRepository;


    @Override
    public CategoryDetailOutput findById(UUID categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found: " + categoryId));

        return CategoryDetailOutput.builder()
                .id(category.getId())
                .name(category.getName())
                .enabled(category.isEnabled())
                .build();
    }

    @Override
    public List<CategoryDetailOutput> findAll() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> CategoryDetailOutput.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .enabled(category.isEnabled())
                        .build())
                .toList();
    }
}
