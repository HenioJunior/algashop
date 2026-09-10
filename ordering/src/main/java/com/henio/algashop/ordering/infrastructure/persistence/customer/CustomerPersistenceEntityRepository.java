package com.henio.algashop.ordering.infrastructure.persistence.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerPersistenceEntityRepository
        extends JpaRepository<CustomerPersistenceEntity, Long> {

    Optional<CustomerPersistenceEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
