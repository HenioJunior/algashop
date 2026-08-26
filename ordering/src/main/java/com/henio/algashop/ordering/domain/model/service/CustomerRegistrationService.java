package com.henio.algashop.ordering.domain.model.service;

import com.henio.algashop.ordering.domain.model.entity.Customer;
import com.henio.algashop.ordering.domain.model.exception.CustomerEmailIsInUseException;
import com.henio.algashop.ordering.domain.model.repository.Customers;
import com.henio.algashop.ordering.domain.model.valueobject.*;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomerRegistrationService {

    private Customers customers;

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
