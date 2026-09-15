package com.henio.algashop.ordering.application.order.management;

import com.henio.algashop.ordering.domain.model.order.Order;
import com.henio.algashop.ordering.domain.model.order.OrderId;
import com.henio.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.henio.algashop.ordering.domain.model.order.Orders;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderManagementApplicationService {

    private final Orders orders;

    public void cancel(String rawOrderId) {
        Objects.requireNonNull(rawOrderId, "rawOrderId must not be null");
        OrderId orderId = new OrderId(TSID.from(rawOrderId));

        Order order = orders.ofId(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));

        order.cancel();

        orders.add(order);
    }

    public void markAsPaid(String rawOrderId) {
        Objects.requireNonNull(rawOrderId, "rawOrderId must not be null");
        OrderId orderId = new OrderId(TSID.from(rawOrderId));

        Order order = orders.ofId(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));

        order.markAsPaid();

        orders.add(order);
    }

    public void markAsReady(String rawOrderId) {
        Objects.requireNonNull(rawOrderId, "rawOrderId must not be null");
        OrderId orderId = new OrderId(TSID.from(rawOrderId));

        Order order = orders.ofId(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));

        order.markAsReady();

        orders.add(order);
    }
}
