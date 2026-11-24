package com.henio.algashop.product.catalog.presentaion;

import com.henio.algashop.product.catalog.application.category.management.CategoryInput;
import com.henio.algashop.product.catalog.application.category.management.CategoryManagementApplicationService;
import com.henio.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.henio.algashop.product.catalog.application.category.query.CategoryQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryQueryService categoryQueryService;
    private final CategoryManagementApplicationService categoryManagementApplicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDetailOutput createCategory(@RequestBody @Valid CategoryInput input) {
        UUID categoryId = categoryManagementApplicationService.create(input);

        return findById(categoryId);
    }

    @GetMapping("/{categoryId}")
    public CategoryDetailOutput findById(@PathVariable UUID categoryId) {
        return categoryQueryService.findById(categoryId);
    }

    @GetMapping
    public List<CategoryDetailOutput> findAll() {
        return categoryQueryService.findAll();
    }

    @PutMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCategory(@PathVariable UUID categoryId, @RequestBody @Valid CategoryInput input) {
        categoryManagementApplicationService.update(categoryId, input);
    }

    @DeleteMapping("/{categoryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable UUID categoryId) {
        categoryManagementApplicationService.disable(categoryId);
    }
}
