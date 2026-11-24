package com.henio.algashop.product.catalog.application.product.query;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductQueryService {
    ProductDetailOutput findById(UUID productId);
    Page<ProductDetailOutput> findAllPage(Pageable pageable);
}
