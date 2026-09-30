package com.henio.algashop.ordering.domain.model.customer.service;

import com.henio.algashop.ordering.domain.model.commons.*;
import com.henio.algashop.ordering.domain.model.customer.BirthDate;
import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.domain.model.customer.Customers;
import com.henio.algashop.ordering.domain.model.customer.exception.CustomerEmailIsInUseException;
import com.henio.algashop.ordering.domain.model.shared.DomainService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainService
public class CustomerRegistrationService {

    private final Customers customers;

    public Customer register(
            FullName fullName,
            BirthDate birthDate,
            Email email,
            Phone phone,
            Document document,
            boolean promotionNotificationsAllowed,
            Address address
    ) {

        verifyEmailUniqueness(email);

        return Customer.brandNew()
                .fullName(fullName)
                .birthDate(birthDate)
                .email(email)
                .phone(phone)
                .document(document)
                .promotionNotificationsAllowed(promotionNotificationsAllowed)
                .address(address)
                .build();
    }

    public void changeEmail(Customer customer, String newEmail) {
        verifyEmailUniqueness(new Email(newEmail));
        customer.changeEmail(new Email(newEmail));
    }

    private void verifyEmailUniqueness(Email email) {
        if(!customers.isEmailUnique(email)) {
            throw new CustomerEmailIsInUseException();
        }
    }
}
