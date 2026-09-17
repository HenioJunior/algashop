package com.henio.algashop.ordering.application.order.management;

import com.henio.algashop.ordering.application.customer.loyaltypoints.CustomerLoyaltyPointsApplicationService;
import com.henio.algashop.ordering.application.order.notification.*;
import com.henio.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.henio.algashop.ordering.domain.model.customer.Customers;
import com.henio.algashop.ordering.domain.model.order.*;
import com.henio.algashop.ordering.infrastructure.listener.order.OrderEventListener;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class OrderManagementApplicationServiceIT {

    @MockitoSpyBean
    private OrderEventListener orderEventListener;

    @MockitoSpyBean
    private OrderNotificationService orderNotificationService;

    @MockitoSpyBean
    private CustomerLoyaltyPointsApplicationService customerLoyaltyPointsApplicationService;

    @Autowired
    private OrderManagementApplicationService service;

    @Autowired
    private Orders orders;

    @Autowired
    private Customers customers;

    @BeforeEach
    public void setup() {
        if (!customers.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customers.add(CustomerTestDataBuilder.existingCustomer().build());
        }
    }

    @Test
    void shouldCancelOrderSuccessfully() {
        Order order = OrderTestDataBuilder.anOrder().build();

        orders.add(order);

        service.cancel(order.id().toString());

        Optional<Order> updatedOrder = orders.ofId(order.id());

        assertThat(updatedOrder)
                .isPresent()
                .get()
                .satisfies(updated -> {
                    Assertions.assertThat(updated.status())
                            .isEqualTo(OrderStatus.CANCELED);

                    Assertions.assertThat(updated.canceledAt())
                            .isNotNull();
                });

        Mockito.verify(orderNotificationService)
                .notifyOrder(
                        Mockito.any(NotifyOrderCanceledInput.class)
                );

        Mockito.verify(orderEventListener)
                .handleOrder(Mockito.any(OrderCanceledEvent.class));
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenCancellingNonExistingOrder() {
        String nonExistingOrderId = new OrderId().toString();

        Assertions.assertThatExceptionOfType(OrderNotFoundException.class)
                .isThrownBy(() -> service.cancel(nonExistingOrderId));
    }

    @Test
    void shouldThrowOrderStatusCannotBeChangedExceptionWhenCancellingAlreadyCanceledOrder() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).build();
        orders.add(order);

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> service.cancel(order.id().toString()));
    }

    @Test
    void shouldMarkOrderAsPaidSuccessfully() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        orders.add(order);

        service.markAsPaid(order.id().toString());

        Optional<Order> updatedOrder = orders.ofId(order.id());

        assertThat(updatedOrder).isPresent();
        assertThat(updatedOrder.get().status()).isEqualTo(OrderStatus.PAID);
        assertThat(updatedOrder.get().paidAt()).isNotNull();

        Mockito.verify(orderNotificationService)
                .notifyOrder(
                        Mockito.any(NotifyOrderPaidInput.class)
                );

        Mockito.verify(orderEventListener)
                .handleOrder(Mockito.any(OrderPaidEvent.class));
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenMarkingNonExistingOrderAsPaid() {
        String nonExistingOrderId = new OrderId().toString();

        Assertions.assertThatExceptionOfType(OrderNotFoundException.class)
                .isThrownBy(() -> service.markAsPaid(nonExistingOrderId));
    }

    @Test
    void shouldThrowOrderStatusCannotBeChangedExceptionWhenMarkingAlreadyPaidOrderAsPaid() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        orders.add(order);

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> service.markAsPaid(order.id().toString()));
    }

    @Test
    void shouldThrowOrderStatusCannotBeChangedExceptionWhenMarkingCanceledOrderAsPaid() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.CANCELED).build();
        orders.add(order);

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> service.markAsPaid(order.id().toString()));
    }

    @Test
    void shouldMarkOrderAsReadySuccessfully() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();
        orders.add(order);

        service.markAsReady(order.id().toString());

        Optional<Order> updatedOrder = orders.ofId(order.id());

        assertThat(updatedOrder).isPresent();
        assertThat(updatedOrder.get().status()).isEqualTo(OrderStatus.READY);
        assertThat(updatedOrder.get().readyAt()).isNotNull();

        Mockito.verify(orderNotificationService)
                .notifyOrder(
                        Mockito.any(NotifyOrderReadyInput.class)
                );

        Mockito.verify(orderEventListener)
                .handleOrder(Mockito.any(OrderReadyEvent.class));

        Mockito.verify(customerLoyaltyPointsApplicationService)
                .addLoyaltyPoints(order.customerId().toString(), order.id().toString());
    }

    @Test
    void shouldThrowOrderNotFoundExceptionWhenMarkingNonExistingOrderAsReady() {
        String nonExistingOrderId = new OrderId().toString();

        Assertions.assertThatExceptionOfType(OrderNotFoundException.class)
                .isThrownBy(() -> service.markAsReady(nonExistingOrderId));
    }

    @Test
    void shouldThrowOrderStatusCannotBeChangedExceptionWhenMarkingAlreadyReadyOrderAsReady() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.READY).build();
        orders.add(order);

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> service.markAsReady(order.id().toString()));
    }

    @Test
    void shouldThrowOrderStatusCannotBeChangedExceptionWhenMarkingPlacedOrderAsReady() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        orders.add(order);

        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(() -> service.markAsReady(order.id().toString()));
    }

}