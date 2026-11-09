package com.henio.algashop.ordering.presentation;

import com.henio.algashop.ordering.application.order.query.OrderDetailOutput;
import com.henio.algashop.ordering.application.order.query.OrderFilter;
import com.henio.algashop.ordering.application.order.query.OrderQueryService;
import com.henio.algashop.ordering.application.order.query.OrderSummaryOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderQueryService orderQueryService;

    @GetMapping("/{orderId}")
    public OrderDetailOutput findById(@PathVariable String orderId) {
        return orderQueryService.findById(orderId);
    }

    @GetMapping
    public Page<OrderSummaryOutput> filter(OrderFilter filter) {
        return orderQueryService.filter(filter);
    }
}
