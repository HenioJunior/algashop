package com.henio.algashop.product.catalog.infrastructure.repository;

import com.henio.algashop.product.catalog.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
}
