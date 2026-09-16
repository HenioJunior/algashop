package com.henio.algashop.ordering.application.customer.notification;

public interface CustomerNotificationService {
    void notifyNewRegistration(NotifyNewRegistrationInput input);

    record NotifyNewRegistrationInput(
            String customerId,
            String firstName,
            String email) {}
}
