package com.henio.algashop.product.catalog.infrastructure.persistence;

import com.henio.algashop.product.catalog.application.ResourceNotFoundException;
import com.henio.algashop.product.catalog.application.product.query.CategoryMinimalOutput;
import com.henio.algashop.product.catalog.application.product.query.ProductDetailOutput;
import com.henio.algashop.product.catalog.application.product.query.ProductQueryService;
import com.henio.algashop.product.catalog.domain.model.Category;
import com.henio.algashop.product.catalog.domain.model.Product;
import com.henio.algashop.product.catalog.infrastructure.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductQueryServiceImpl implements ProductQueryService {

    private final ProductRepository productRepository;


    @Override
    public ProductDetailOutput findById(UUID productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product not found: " + productId));
        return ProductDetailOutput.builder()
                .id(product.getId())
                .addedAt(product.getAddedAt())
                .name(product.getName())
                .brand(product.getBrand())
                .regularPrice(product.getRegularPrice())
                .salePrice(product.getSalePrice())
                .inStock(product.getInStock())
                .enabled(product.getEnabled())
                .description(product.getDescription())
                .category(toCategoryOutput(product.getCategory()))
                .build();
    }

    private static CategoryMinimalOutput toCategoryOutput(Category category) {
        return CategoryMinimalOutput.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    @Override
    public Page<ProductDetailOutput> findAllPage(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::toProductDetailOutput);
    }

    private ProductDetailOutput toProductDetailOutput(Product product) {
        return ProductDetailOutput.builder()
                .id(product.getId())
                .addedAt(product.getAddedAt())
                .name(product.getName())
                .brand(product.getBrand())
                .regularPrice(product.getRegularPrice())
                .salePrice(product.getSalePrice())
                .inStock(product.getInStock())
                .enabled(product.getEnabled())
                .description(product.getDescription())
                .category(toCategoryMinimalOutput(product.getCategory()))
                .build();
    }

    private CategoryMinimalOutput toCategoryMinimalOutput(Category category) {
        Objects.requireNonNull(category);

        CategoryMinimalOutput output = new CategoryMinimalOutput();

        output.setId(category.getId());
        output.setName(category.getName());

        return output;
    }

}
