package com.henio.algashop.ordering.infrastructure.shipping.client.rapidex;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface RapidexApiClient {

    @PostExchange("/api/delivery-cost")
    DeliveryCostResponse calculate(@RequestBody DeliveryCostRequest deliveryCostRequest);
}
