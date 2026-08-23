package com.henio.algashop.ordering.infrastructure.persistence.adapter;


import com.henio.algashop.ordering.domain.model.entity.*;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.OrderItemPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.OrderItemPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntityTestDataBuilder;
import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import com.henio.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import io.hypersistence.tsid.TSID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({
        OrdersPersistenceAdapter.class,
        OrderPersistenceAssembler.class,
        OrderItemPersistenceAssembler.class,
        OrderPersistenceDisassembler.class,
        OrderItemPersistenceDisassembler.class,
        SpringDataAuditingConfig.class
})
class OrdersPersistenceAdapterIT {

    private final OrdersPersistenceAdapter persistenceAdapter;
    private final OrderPersistenceEntityRepository entityRepository;
    private final CustomerPersistenceEntityRepository customerEntityRepository;

    @Autowired
    OrdersPersistenceAdapterIT(OrdersPersistenceAdapter persistenceAdapter, OrderPersistenceEntityRepository entityRepository, CustomerPersistenceEntityRepository customerEntityRepository) {
        this.persistenceAdapter = persistenceAdapter;
        this.entityRepository = entityRepository;
        this.customerEntityRepository = customerEntityRepository;
    }

    @Test
    void shouldUpdateAndKeepPersistenceEntityState() {
        CustomerPersistenceEntity customerEntity =
                CustomerPersistenceEntityTestDataBuilder.aCustomer().build();

        customerEntityRepository.saveAndFlush(customerEntity);


        Order order = OrderTestDataBuilder.anOrder()
                .customerId(new CustomerId(TSID.from(customerEntity.getId())))
                .status(OrderStatus.PLACED)
                .build();

        long orderId = order.id().value().toLong();

        persistenceAdapter.add(order);

        OrderPersistenceEntity persistenceEntity = entityRepository.findById(orderId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getStatus())
                .isEqualTo(OrderStatus.PLACED.name());

        assertAuditFields(persistenceEntity);

        order = persistenceAdapter.ofId(order.id()).orElseThrow();
        order.markAsPaid();

        persistenceAdapter.add(order);

        persistenceEntity =
                entityRepository.findById(orderId).orElseThrow();

        Assertions.assertThat(persistenceEntity.getStatus())
                .isEqualTo(OrderStatus.PAID.name());

        assertAuditFields(persistenceEntity);
    }

    private void assertAuditFields(OrderPersistenceEntity entity) {
        Assertions.assertThat(entity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(entity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(entity.getLastModifiedByUserId()).isNotNull();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldAddFindAndNotFailWhenNoTransaction() {
        CustomerPersistenceEntity customerEntity =
                CustomerPersistenceEntityTestDataBuilder.aCustomer().build();

        customerEntityRepository.saveAndFlush(customerEntity);

        Order order = OrderTestDataBuilder.anOrder().customerId(new CustomerId(TSID.from(customerEntity.getId()))).build();

        persistenceAdapter.add(order);

        Assertions.assertThatNoException().isThrownBy(() -> persistenceAdapter.ofId(order.id()).orElseThrow());
    }
}