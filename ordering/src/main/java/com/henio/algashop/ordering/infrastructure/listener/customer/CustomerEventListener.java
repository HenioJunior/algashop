package com.henio.algashop.ordering.infrastructure.listener.customer;

import com.henio.algashop.ordering.application.customer.notification.CustomerNotificationService;
import com.henio.algashop.ordering.domain.model.customer.CustomerArchivedEvent;
import com.henio.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import com.henio.algashop.ordering.application.customer.notification.CustomerNotificationService.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerEventListener {

    private final CustomerNotificationService customerNotificationService;

    @EventListener
    public void listen(CustomerRegisteredEvent event) {
        log.info("CustomerRegisteredEvent");
        NotifyNewRegistrationInput input = new NotifyNewRegistrationInput(
                event.customerId().toString(),
                event.fullName().firstName(),
                event.email().toString()
        );
        customerNotificationService.notifyNewRegistration(input);
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
