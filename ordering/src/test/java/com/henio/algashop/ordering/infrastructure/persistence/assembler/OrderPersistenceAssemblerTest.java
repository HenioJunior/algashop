package com.henio.algashop.ordering.infrastructure.persistence.assembler;

import com.henio.algashop.ordering.domain.model.entity.Order;
import com.henio.algashop.ordering.domain.model.entity.OrderItem;
import com.henio.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.henio.algashop.ordering.infrastructure.persistence.entity.*;
import com.henio.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
class OrderPersistenceAssemblerTest {

    @Mock
    private CustomerPersistenceEntityRepository customerRepository;

    private OrderPersistenceAssembler assembler;

    private OrderItemPersistenceAssembler itemAssembler;

    @BeforeEach
    public void setUp() {
        itemAssembler = new OrderItemPersistenceAssembler();

        assembler = new OrderPersistenceAssembler(
                itemAssembler,
                customerRepository
        );

        Mockito.when(customerRepository.getReferenceById(Mockito.anyLong()))
                .thenAnswer(invocation -> {
                    Long customerId = invocation.getArgument(0);

                    return CustomerPersistenceEntityTestDataBuilder
                            .aCustomer()
                            .id(customerId)
                            .build();
                });
    }


    @Test
    void shouldConvertToDomain() {
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderPersistenceEntity orderPersistenceEntity = assembler.fromDomain(order);


        assertThat(orderPersistenceEntity).satisfies(
                p-> assertThat(p.getId()).isEqualTo(order.id().value().toLong()),
                p-> assertThat(p.getCustomer()).isEqualTo(customerRepository.getReferenceById(order.customerId().value().toLong())),
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

        assertThat(order.items()).isEmpty();
        assertThat(orderPersistenceEntity.getItems()).isNotEmpty();

        assembler.merge(orderPersistenceEntity, order);

        assertThat(order.items()).isEmpty();
        assertThat(orderPersistenceEntity.getItems()).isEmpty();
    }

    @Test
    void givenOrderWithItems_shouldAddToPersistenceEntityItems() {
        Order order = OrderTestDataBuilder.anOrder().withItems(true).build();
        OrderPersistenceEntity orderPersistenceEntity = new OrderPersistenceEntity();

        assertThat(order.items()).isNotEmpty();
        assertThat(orderPersistenceEntity.getItems()).isEmpty();

        assembler.merge(orderPersistenceEntity, order);

        assertThat(orderPersistenceEntity.getItems()).isNotEmpty();
        assertThat(orderPersistenceEntity.getItems().size()).isEqualTo(order.items().size());
    }

    @Test
    void givenOrderWithRemovedItem_whenMerge_thenShouldRemoveItemFromPersistenceEntity() {
        Order order = OrderTestDataBuilder.anOrder()
                .withItems(true)
                .build();

        assertThat(order.items()).hasSize(2);

        Set<OrderItemPersistenceEntity> persistedItems =
                itemAssembler.toItemsEntity(order.items());

        OrderPersistenceEntity persistenceEntity =
                OrderPersistenceEntityTestDataBuilder
                        .existingOrderBuilder()
                        .items(persistedItems)
                        .build();

        OrderItem removedItem = order.items()
                .iterator()
                .next();

        order.removeItem(removedItem.id());

        assembler.merge(persistenceEntity, order);

        assertThat(persistenceEntity.getItems())
                .hasSize(1)
                .noneMatch(item ->
                        item.getId().equals(
                                removedItem.id().value().toLong()
                        )
                );
    }
}