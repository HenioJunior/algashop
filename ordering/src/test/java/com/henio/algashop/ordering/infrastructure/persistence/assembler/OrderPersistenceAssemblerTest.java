package com.henio.algashop.ordering.infrastructure.persistence.assembler;

import com.henio.algashop.ordering.domain.model.entity.Order;
import com.henio.algashop.ordering.domain.model.entity.OrderItem;
import com.henio.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class OrderPersistenceAssemblerTest {

    private final OrderPersistenceAssembler assembler = new OrderPersistenceAssembler();

    @Test
    void shouldConvertToDomain() {
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderPersistenceEntity orderPersistenceEntity = assembler.fromDomain(order);

        assertThat(orderPersistenceEntity).satisfies(
                p-> assertThat(p.getId()).isEqualTo(order.id().value().toLong()),
                p-> assertThat(p.getCustomerId()).isEqualTo(order.customerId().value().toLong()),
                p -> assertThat(p.getTotalAmount()).isEqualTo(order.totalAmount().value()),
                p -> assertThat(p.getTotalItems()).isEqualTo(order.totalItems().value()),
                p -> assertThat(p.getStatus()).isEqualTo(order.status().name()),
                p -> assertThat(p.getPaymentMethod()).isEqualTo(order.paymentMethod().name()),
                p -> assertThat(p.getPlacedAt()).isEqualTo(order.placedAt()),
                p -> assertThat(p.getPaidAt()).isEqualTo(order.paidAt()),
                p -> assertThat(p.getCanceledAt()).isEqualTo(order.canceledAt()),
                p -> assertThat(p.getReadyAt()).isEqualTo(order.readyAt())
        );
    }

    @Test
    void givenOrderWithNotItems_shouldRemovePersistenceEntityItems() {
        Order order = OrderTestDataBuilder.anOrder().withItems(false).build();
        OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrder();

        Assertions.assertThat(order.items()).isEmpty();
        Assertions.assertThat(orderPersistenceEntity.getItems()).isNotEmpty();

        assembler.merge(orderPersistenceEntity, order);

        Assertions.assertThat(order.items()).isEmpty();
        Assertions.assertThat(orderPersistenceEntity.getItems()).isEmpty();
    }

    @Test
    void givenOrderWithItems_shouldAddToPersistenceEntityItems() {
        Order order = OrderTestDataBuilder.anOrder().withItems(true).build();
        OrderPersistenceEntity orderPersistenceEntity = new OrderPersistenceEntity();

        Assertions.assertThat(order.items()).isNotEmpty();
        Assertions.assertThat(orderPersistenceEntity.getItems()).isEmpty();

        assembler.merge(orderPersistenceEntity, order);

        Assertions.assertThat(orderPersistenceEntity.getItems()).isNotEmpty();
        Assertions.assertThat(orderPersistenceEntity.getItems().size()).isEqualTo(order.items().size());
    }

    @Test
    void givenOrderWithItems_whenMerge_shouldRemoveMergeCorrectly() {
        Order order = OrderTestDataBuilder.anOrder().withItems(true).build();

        Assertions.assertThat(order.items().size()).isEqualTo(2);

        Set<OrderItemPersistenceEntity> orderItemPersistenceEntities = assembler.toItemsEntity(order.items());

        OrderPersistenceEntity orderPersistenceEntity = OrderPersistenceEntityTestDataBuilder.existingOrderBuilder()
                .items(orderItemPersistenceEntities)
                .build();

        OrderItem orderItem = order.items().iterator().next();
        order.removeItem(orderItem.id());

        assembler.merge(orderPersistenceEntity, order);

        Assertions.assertThat(orderPersistenceEntity.getItems()).isNotEmpty();
        Assertions.assertThat(orderPersistenceEntity.getItems().size()).isEqualTo(order.items().size());
    }
}