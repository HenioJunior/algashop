package com.henio.algashop.ordering.domain.model.repository;

import com.henio.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.henio.algashop.ordering.domain.model.entity.Order;
import com.henio.algashop.ordering.domain.model.entity.OrderStatus;
import com.henio.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.henio.algashop.ordering.infrastructure.persistence.adapter.CustomersPersistenceAdapter;
import com.henio.algashop.ordering.infrastructure.persistence.adapter.OrdersPersistenceAdapter;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.OrderItemPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.OrderItemPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.repository.OrderPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@DataJpaTest
@Import({
        OrdersPersistenceAdapter.class,
        OrderPersistenceAssembler.class,
        OrderItemPersistenceAssembler.class,
        OrderPersistenceDisassembler.class,
        OrderItemPersistenceDisassembler.class,
        CustomersPersistenceAdapter.class,
        CustomerPersistenceAssembler.class,
        CustomerPersistenceDisassembler.class
})
class OrdersIT {

    private final Orders orders;
    private final OrderPersistenceEntityRepository repository;
    private final Customers customers;
    private final TransactionTemplate newTransaction;

    @Autowired
    public OrdersIT(Orders orders,
                    Customers customers,
                    PlatformTransactionManager transactionManager,
                    OrderPersistenceEntityRepository repository) {
        this.orders = orders;
        this.repository = repository;
        this.customers = customers;
        this.newTransaction = new TransactionTemplate(transactionManager);
        this.newTransaction.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    @BeforeEach
    void cleanup() {
        inNewTransaction(() -> {
            repository.deleteAll();
            if(!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)){
                customers.add(CustomerTestDataBuilder.existingCustomer().build());
            }
        });
    }

    @Test
    void shouldPersistAndFind() {
        Order order = OrderTestDataBuilder.anOrder()
                .build();

        OrderId orderId = order.id();

        orders.add(order);

        Optional<Order> savedOrder = orders.ofId(orderId);

        Assertions.assertThat(savedOrder)
                .isPresent()
                .contains(order);
    }

    @Test
    void shouldUpdateExistingOrder() {
        Order order = OrderTestDataBuilder.anOrder()
                .status(OrderStatus.PLACED).build();
        orders.add(order);

        order = orders.ofId(order.id()).orElseThrow();
        order.markAsPaid();

        orders.add(order);

        order = orders.ofId(order.id()).orElseThrow();

        Assertions.assertThat(order.status()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingStaleOrder(){
        //T0
        OrderId orderId = inNewTransaction(() -> {
            Order order = OrderTestDataBuilder.anOrder()
                    .status(OrderStatus.PLACED)
                    .build();

            orders.add(order);

            return order.id();
        });

        Assertions.assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class)
                .isThrownBy(() -> inNewTransaction(() -> {
                    // T1: carrega o pedido em sua própria transação
                    Order orderT1 = orders.ofId(orderId).orElseThrow();

                    // T2: em outra transação separada, salva primeiro
                    inNewTransaction(() -> {
                        Order orderT2 = orders.ofId(orderId).orElseThrow();
                        orderT2.markAsPaid();
                        orders.add(orderT2);
                    });

                    // T1 tenta salvar com versão obsoleta
                    orderT1.cancel();
                    orders.add(orderT1);
                }));

        // Verifica que a atualização de T2 prevaleceu
        Order savedOrder = orders.ofId(orderId).orElseThrow();
        Assertions.assertThat(savedOrder.canceledAt()).isNull();
        Assertions.assertThat(savedOrder.paidAt()).isNotNull();
    }

    private <T> T inNewTransaction(Supplier<T> callback) {
        return newTransaction.execute(status -> callback.get());
    }

    private void inNewTransaction(Runnable callback) {
        newTransaction.executeWithoutResult(status -> callback.run());
    }

    @Test
    public void shouldCountExistingOrders() {
        Assertions.assertThat(orders.count()).isZero();

        Order order1 = OrderTestDataBuilder.anOrder().build();
        Order order2 = OrderTestDataBuilder.anOrder().build();

        orders.add(order1);
        orders.add(order2);

        Assertions.assertThat(orders.count()).isEqualTo(2L);
    }

    @Test
    public void shouldReturnIfOrderExists() {
        Order order = OrderTestDataBuilder.anOrder()
                .build();
        orders.add(order);

        Assertions.assertThat(orders.exists(order.id())).isTrue();
        Assertions.assertThat(orders.exists(new OrderId())).isFalse();

    }

    @Test
    void shouldListExistingOrdersByYear(){
        orders.add(
                OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build()
        );
        orders.add(
                OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build()
        );
        orders.add(
                OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).build()
        );
        orders.add(
                OrderTestDataBuilder.anOrder().status(OrderStatus.DRAFT).build()
        );

        CustomerId customerId = CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

        List<Order> updatedOrders = orders.placedByCustomerInYear(customerId, Year.now());
        Assertions.assertThat(updatedOrders).isNotEmpty();
        Assertions.assertThat(updatedOrders).hasSize(2);

        updatedOrders = orders.placedByCustomerInYear(customerId, Year.now().minusYears(1));
        Assertions.assertThat(updatedOrders).isEmpty();

        updatedOrders = orders.placedByCustomerInYear(new CustomerId(), Year.now());
        Assertions.assertThat(updatedOrders).isEmpty();
    }
}