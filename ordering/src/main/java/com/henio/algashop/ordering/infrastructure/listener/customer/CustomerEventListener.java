package com.henio.algashop.ordering.infrastructure.listener.customer;

import com.henio.algashop.ordering.domain.model.customer.CustomerArchivedEvent;
import com.henio.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomerEventListener {

    @EventListener
    public void listen(CustomerRegisteredEvent event) {
        log.info(
                "Customer registered. customerId={}, registeredAt={}",
                event.customerId(),
                event.registeredAt()
        );
    }

    @EventListener
    public void listen(CustomerArchivedEvent event) {
        log.info(
                "Customer archived. customerId={}, archivedAt={}",
                event.customerId(),
                event.archivedAt()
        );
    }
}
