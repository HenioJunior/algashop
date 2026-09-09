package com.henio.algashop.ordering.domain.model.customer;

import com.henio.algashop.ordering.domain.model.commons.*;
import com.henio.algashop.ordering.domain.model.shared.DomainService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainService
public class CustomerRegistrationService {

    private final Customers customers;

    public Customer register(FullName fullName, BirthDate birthDate, Email email, Phone phone, Document document,
                             boolean promotionNotificationsAllowed, Address address) {
        Customer customer = Customer.brandNew()
                .fullName(fullName)
                .birthDate(birthDate)
                .email(email)
                .phone(phone)
                .document(document)
                .promotionNotificationsAllowed(promotionNotificationsAllowed)
                .address(address)
                .build();
        verifyEmailUniqueness(customer.email(), customer.id());
        return customer;
    }

    public void changeEmail(Customer customer, Email newEmail) {
        verifyEmailUniqueness(newEmail, customer.id());
        customer.changeEmail(newEmail);
    }

    private void verifyEmailUniqueness(Email email, CustomerId id) {
        if(!customers.isEmailUnique(email, id)) {
            throw new CustomerEmailIsInUseException();
        }
    }
}
