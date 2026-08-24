package com.henio.algashop.ordering.infrastructure.persistence.repository;

import com.henio.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerPersistenceEntityRepository extends JpaRepository<CustomerPersistenceEntity, Long> {
    Optional<CustomerPersistenceEntity> findByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long customerId);
}
