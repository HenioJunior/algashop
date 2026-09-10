package com.henio.algashop.ordering.domain.model.customer;

import com.henio.algashop.ordering.domain.model.commons.*;
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

    private void verifyEmailUniqueness(Email email) {
        if(!customers.isEmailUnique(email)) {
            throw new CustomerEmailIsInUseException();
        }
    }
}
