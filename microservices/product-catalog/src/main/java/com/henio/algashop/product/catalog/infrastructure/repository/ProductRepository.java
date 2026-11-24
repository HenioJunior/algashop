package com.henio.algashop.product.catalog.infrastructure.repository;

import com.henio.algashop.product.catalog.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
