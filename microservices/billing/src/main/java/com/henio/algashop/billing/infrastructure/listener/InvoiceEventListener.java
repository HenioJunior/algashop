package com.henio.algashop.billing.infrastructure.listener;

import com.henio.algashop.billing.domain.model.invoice.InvoiceCanceledEvent;
import com.henio.algashop.billing.domain.model.invoice.InvoiceIssuedEvent;
import com.henio.algashop.billing.domain.model.invoice.InvoicePaidEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InvoiceEventListener {

    @EventListener
    public void listen(InvoiceIssuedEvent event) {
        log.info("Invoice issued: {}", event);
    }

    @EventListener
    public void listen(InvoicePaidEvent event) {
        log.info("Invoice paid: {}", event);
    }

    @EventListener
    public void listen(InvoiceCanceledEvent event) {
        log.info("Invoice canceled: {}", event);
    }
}
