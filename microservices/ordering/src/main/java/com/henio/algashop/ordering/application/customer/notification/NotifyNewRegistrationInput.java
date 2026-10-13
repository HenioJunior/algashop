package com.henio.algashop.ordering.application.customer.notification;

public record NotifyNewRegistrationInput(
        String customerId,
        String firstName,
        String email
) {}
