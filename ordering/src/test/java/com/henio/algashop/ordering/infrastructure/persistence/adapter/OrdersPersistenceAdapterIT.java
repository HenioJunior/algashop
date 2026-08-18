package com.henio.algashop.ordering.infrastructure.persistence.adapter;


import com.henio.algashop.ordering.domain.model.entity.Order;
import com.henio.algashop.ordering.domain.model.entity.OrderStatus;
import com.henio.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({
        OrdersPersistenceAdapter.class,
        OrderPersistenceAssembler.class,
        OrderPersistenceDisassembler.class,
        SpringDataAuditingConfig.class
})
class OrdersPersistenceAdapterIT {

    private OrdersPersistenceAdapter persistenceAdapter;
    private OrderPersistenceEntityRepository entityRepository;

    @Autowired
    OrdersPersistenceAdapterIT(OrdersPersistenceAdapter persistenceAdapter, OrderPersistenceEntityRepository entityRepository) {
        this.persistenceAdapter = persistenceAdapter;
        this.entityRepository = entityRepository;
    }

    @Test
    void shouldUpdateAndKeepPersistenceEntityState() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        long orderId = order.id().value().toLong();

        persistenceAdapter.add(order);

        OrderPersistenceEntity orderPersistenceEntity = entityRepository.findById(orderId).orElseThrow();

        Assertions.assertThat(orderPersistenceEntity.getStatus()).isEqualTo("PLACED");

        Assertions.assertThat(orderPersistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(orderPersistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(orderPersistenceEntity.getLastModifiedByUserId()).isNotNull();

        order = persistenceAdapter.ofId(order.id()).orElseThrow();
        order.markAsPaid();
        persistenceAdapter.add(order);

        orderPersistenceEntity = entityRepository.findById(orderId).orElseThrow();

        Assertions.assertThat(orderPersistenceEntity.getStatus()).isEqualTo(OrderStatus.PAID.name());

        Assertions.assertThat(orderPersistenceEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(orderPersistenceEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(orderPersistenceEntity.getLastModifiedByUserId()).isNotNull();
    }
}