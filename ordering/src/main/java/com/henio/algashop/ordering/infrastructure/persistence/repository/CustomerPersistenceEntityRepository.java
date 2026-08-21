package com.henio.algashop.ordering.infrastructure.persistence.repository;

import com.henio.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerPersistenceEntityRepository extends JpaRepository<CustomerPersistenceEntity, Long> {
}
