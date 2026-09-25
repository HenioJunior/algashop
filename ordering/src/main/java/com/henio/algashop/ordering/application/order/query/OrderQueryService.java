package com.henio.algashop.ordering.application.order.query;

import com.henio.algashop.ordering.application.utility.PageFilter;
import org.springframework.data.domain.Page;

public interface OrderQueryService {
    OrderDetailOutput findById(String orderId);
    Page<OrderSummaryOutput> filter(PageFilter filter);
}
