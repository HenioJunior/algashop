package com.henio.algashop.ordering.infrastructure.persistence.repository;

import com.henio.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.henio.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntityTestDataBuilder;
import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(SpringDataAuditingConfig.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class OrderPersistenceEntityRepositoryIT {

    private final OrderPersistenceEntityRepository orderPersistenceEntityRepository;
    private final CustomerPersistenceEntityRepository customerRepository;


    @Test
    void shouldPersistOrder(){
        OrderPersistenceEntity entity = newOrderWithPersistedCustomer();
        long orderId = entity.getId();

        orderPersistenceEntityRepository.saveAndFlush(entity);
        Assertions.assertThat(orderPersistenceEntityRepository.existsById(orderId)).isTrue();

        OrderPersistenceEntity savedEntity = orderPersistenceEntityRepository.findById(orderId).orElseThrow();
        Assertions.assertThat(savedEntity.getItems()).isNotEmpty();
    }

    @Test
    void shouldCountOrders(){
        Assertions.assertThat(orderPersistenceEntityRepository.count()).isZero();
    }

    @Test
    void shouldSetAuditingValues() {
        OrderPersistenceEntity entity = newOrderWithPersistedCustomer();
        entity = orderPersistenceEntityRepository.saveAndFlush(entity);

        Assertions.assertThat(entity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(entity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(entity.getLastModifiedByUserId()).isNotNull();
    }

    private OrderPersistenceEntity newOrderWithPersistedCustomer() {
        CustomerPersistenceEntity customer =
                CustomerPersistenceEntityTestDataBuilder.aCustomer().build();

        customerRepository.saveAndFlush(customer);

        return OrderPersistenceEntityTestDataBuilder.existingOrderBuilder()
                .customer(customer)
                .build();
    }
}