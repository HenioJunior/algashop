package com.henio.algashop.ordering.infrastructure.persistence.repository;

import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderPersistenceEntityRepository extends JpaRepository<OrderPersistenceEntity, Long> {

    @Query("""
    SELECT o
    FROM OrderPersistenceEntity o
    WHERE o.customer.id = :customerId
    AND YEAR(o.placedAt) = :year
    """)
    List<OrderPersistenceEntity> placedByCustomerInYear(
            @Param("customerId") Long customerId,
            @Param("year") Integer year
    );
}
