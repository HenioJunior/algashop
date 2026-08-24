package com.henio.algashop.ordering.infrastructure.persistence.repository;

import com.henio.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShoppingCartPersistenceEntityRepository extends JpaRepository<ShoppingCartPersistenceEntity, Long> {
    Optional<ShoppingCartPersistenceEntity> findByCustomer_Id(Long value);
}
