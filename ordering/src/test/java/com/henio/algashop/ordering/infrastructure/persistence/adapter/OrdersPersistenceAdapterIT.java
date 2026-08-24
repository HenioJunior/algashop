package com.henio.algashop.ordering.infrastructure.persistence.adapter;


import com.henio.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.henio.algashop.ordering.domain.model.entity.Order;
import com.henio.algashop.ordering.domain.model.entity.OrderStatus;
import com.henio.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.OrderItemPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.OrderItemPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DataJpaTest
@Import({
        OrdersPersistenceAdapter.class,
        OrderPersistenceAssembler.class,
        OrderItemPersistenceAssembler.class,
        OrderPersistenceDisassembler.class,
        OrderItemPersistenceDisassembler.class,
        CustomersPersistenceAdapter.class,
        CustomerPersistenceAssembler.class,
        CustomerPersistenceDisassembler.class,
        SpringDataAuditingConfig.class
})
class OrdersPersistenceAdapterIT {

    private final OrdersPersistenceAdapter persistenceAdapter;
    private final OrderPersistenceEntityRepository entityRepository;
    private final CustomersPersistenceAdapter customersPersistenceAdapter;

    @BeforeEach
    void cleanup() {
        if(!customersPersistenceAdapter.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)){
            customersPersistenceAdapter.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    void shouldUpdateAndKeepPersistenceEntityState() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.PLACED)
                .build();

        long orderId = order.id().value().toLong();

        persistenceAdapter.add(order);

        OrderPersistenceEntity persistenceEntity = entityRepository
                .findById(orderId)
                .orElseThrow();

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
        Order order = OrderTestDataBuilder.anOrder().build();
        persistenceAdapter.add(order);
        Assertions.assertThatNoException().isThrownBy(() -> persistenceAdapter.ofId(order.id()).orElseThrow());
    }
}