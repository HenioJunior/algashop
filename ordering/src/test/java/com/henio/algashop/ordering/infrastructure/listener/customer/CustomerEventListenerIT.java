package com.henio.algashop.ordering.infrastructure.listener.customer;

import com.henio.algashop.ordering.application.customer.loyaltypoints.CustomerLoyaltyPointsApplicationService;
import com.henio.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.henio.algashop.ordering.application.customer.notification.NotifyNewRegistrationInput;
import com.henio.algashop.ordering.domain.model.commons.Email;
import com.henio.algashop.ordering.domain.model.commons.FullName;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import com.henio.algashop.ordering.domain.model.order.OrderId;
import com.henio.algashop.ordering.domain.model.order.OrderReadyEvent;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.time.OffsetDateTime;

@SpringBootTest
class CustomerEventListenerIT {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @MockitoSpyBean
    private CustomerEventListener customerEventListener;

    @MockitoSpyBean
    private CustomerNotificationApplicationService notificationApplicationService;

    @MockitoBean
    private CustomerLoyaltyPointsApplicationService loyaltyPointsApplicationService;

    @Test
    void shouldListenOrderReadyEvent() {
        applicationEventPublisher.publishEvent(
                new OrderReadyEvent(
                        new OrderId(),
                        new CustomerId(),
                        OffsetDateTime.now()
                ));

        Mockito.verify(customerEventListener)
                .listen(Mockito.any(OrderReadyEvent.class));

        Mockito.verify(loyaltyPointsApplicationService)
                .addLoyaltyPoints(
                        Mockito.any(String.class),
                        Mockito.any(String.class)
                );
    }

    @Test
    void shouldListenCustomerRegisteredEvent() {
        applicationEventPublisher.publishEvent(new CustomerRegisteredEvent(
                new CustomerId(),
                OffsetDateTime.now(),
                new FullName("John", "Doe"),
                new Email("test@test.com")
        ));

        Mockito.verify(customerEventListener)
                .listen(Mockito.any(CustomerRegisteredEvent.class));

        Mockito.verify(notificationApplicationService)
                .notifyNewRegistration(Mockito
                        .any(NotifyNewRegistrationInput.class));
    }

}