package com.henio.algashop.ordering.infrastructure.persistence.repository;

import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface OrderPersistenceEntityRepository extends JpaRepository<OrderPersistenceEntity, Long> {
    List<OrderPersistenceEntity> findByCustomer_IdAndPlacedAtBetween(
            Long customerId,
            OffsetDateTime start,
            OffsetDateTime end
    );
}
