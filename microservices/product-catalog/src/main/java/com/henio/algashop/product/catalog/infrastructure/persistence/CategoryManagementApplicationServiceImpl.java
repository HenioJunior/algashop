package com.henio.algashop.product.catalog.infrastructure.persistence;

import com.henio.algashop.product.catalog.application.ResourceNotFoundException;
import com.henio.algashop.product.catalog.application.category.management.CategoryInput;
import com.henio.algashop.product.catalog.application.category.management.CategoryManagementApplicationService;
import com.henio.algashop.product.catalog.domain.model.Category;
import com.henio.algashop.product.catalog.infrastructure.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class CategoryManagementApplicationServiceImpl implements CategoryManagementApplicationService {

    private final CategoryRepository categoryRepository;

    @Override
    public UUID create(CategoryInput categoryInput) {
        Category category = Category.builder()
                .name(categoryInput.getName())
                .enabled(categoryInput.isEnabled())
                .build();

        categoryRepository.save(category);

        return category.getId();
    }

    @Override
    public void update(UUID categoryId, CategoryInput categoryInput) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
        category.setName(categoryInput.getName());
        categoryRepository.save(category);
    }

    @Override
    public void disable(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
        category.setEnabled(false);
        categoryRepository.save(category);
    }
}
