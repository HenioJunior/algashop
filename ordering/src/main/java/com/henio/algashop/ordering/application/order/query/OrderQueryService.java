package com.henio.algashop.ordering.application.order.query;

public interface OrderQueryService {
    OrderDetailOutput findById(String orderId);
}
