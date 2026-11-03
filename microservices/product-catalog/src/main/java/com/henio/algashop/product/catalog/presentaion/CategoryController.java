package com.henio.algashop.product.catalog.presentaion;

import com.henio.algashop.product.catalog.application.category.management.CategoryInput;
import com.henio.algashop.product.catalog.application.category.management.CategoryManagementApplicationService;
import com.henio.algashop.product.catalog.application.category.query.CategoryDetailOutput;
import com.henio.algashop.product.catalog.application.category.query.CategoryQueryService;
import com.henio.algashop.product.catalog.application.product.query.PageModel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public PageModel<CategoryDetailOutput> filter(
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "number", required = false) Integer number
    ) {
        return categoryQueryService.filter(size, number);
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
