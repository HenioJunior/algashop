package com.henio.algashop.product.catalog.infrastructure.persistence;

import com.henio.algashop.product.catalog.application.ResourceNotFoundException;
import com.henio.algashop.product.catalog.application.product.management.ProductInput;
import com.henio.algashop.product.catalog.application.product.management.ProductManagementApplicationService;
import com.henio.algashop.product.catalog.domain.model.Category;
import com.henio.algashop.product.catalog.domain.model.Product;
import com.henio.algashop.product.catalog.infrastructure.repository.CategoryRepository;
import com.henio.algashop.product.catalog.infrastructure.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Transactional
@RequiredArgsConstructor
@Service
public class ProductManagementApplicationServiceImpl implements ProductManagementApplicationService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public UUID create(ProductInput productInput) {

        Category category = categoryRepository.findById(productInput.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found: " + productInput.getCategoryId()));

        Product product = Product.builder()
                .addedAt(OffsetDateTime.now())
                .name(productInput.getName())
                .brand(productInput.getBrand())
                .regularPrice(productInput.getRegularPrice())
                .salePrice(productInput.getSalePrice())
                .inStock(false)
                .enabled(true)
                .category(category)
                .description(productInput.getDescription())
                .build();

        productRepository.save(product);

        return product.getId();
    }

    @Override
    public void update(UUID productId, ProductInput productInput) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found: " + productId));

        Category category = categoryRepository.findById(productInput.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found: " + productInput.getCategoryId()));

        product.setName(productInput.getName());
        product.setBrand(productInput.getBrand());
        product.setRegularPrice(productInput.getRegularPrice());
        product.setSalePrice(productInput.getSalePrice());
        product.setInStock(true);
        product.setCategory(category);
        product.setDescription(productInput.getDescription());
        productRepository.save(product);
    }

    @Override
    public void disable(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found: " + productId));
        product.setEnabled(false);
        productRepository.save(product);
    }
}
