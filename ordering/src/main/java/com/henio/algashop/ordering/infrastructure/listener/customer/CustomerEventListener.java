package com.henio.algashop.ordering.infrastructure.listener.customer;

import com.henio.algashop.ordering.application.customer.loyaltypoints.CustomerLoyaltyPointsApplicationService;
import com.henio.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.henio.algashop.ordering.application.customer.notification.NotifyNewRegistrationInput;
import com.henio.algashop.ordering.domain.model.customer.CustomerArchivedEvent;
import com.henio.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import com.henio.algashop.ordering.domain.model.order.OrderReadyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerEventListener {

    private final CustomerNotificationApplicationService customerNotificationApplicationService;

    private final CustomerLoyaltyPointsApplicationService customerLoyaltyPointsApplicationService;

    @EventListener
    public void listen(CustomerRegisteredEvent event) {
        log.info("CustomerRegisteredEvent");
        NotifyNewRegistrationInput input = new NotifyNewRegistrationInput(
                event.customerId().toString(),
                event.fullName().firstName(),
                event.email().toString()
        );
        customerNotificationApplicationService.notifyNewRegistration(input);
    }

    @EventListener
    public void listen(CustomerArchivedEvent event) {
        log.info(
                "Customer archived. customerId={}, archivedAt={}",
                event.customerId(),
                event.archivedAt()
        );
    }

    @EventListener
    public void listen(OrderReadyEvent event) {
        customerLoyaltyPointsApplicationService
                .addLoyaltyPoints(event.customerId().toString(), event.orderId().toString());
    }
}
