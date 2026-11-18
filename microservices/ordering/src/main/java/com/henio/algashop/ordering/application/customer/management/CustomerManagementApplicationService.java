package com.henio.algashop.ordering.application.customer.management;

import com.henio.algashop.ordering.application.commons.AddressData;
import com.henio.algashop.ordering.domain.model.commons.*;
import com.henio.algashop.ordering.domain.model.customer.BirthDate;
import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.customer.Customers;
import com.henio.algashop.ordering.domain.model.customer.exception.CustomerAlreadyArchivedException;
import com.henio.algashop.ordering.domain.model.customer.exception.CustomerNotFoundException;
import com.henio.algashop.ordering.domain.model.customer.service.CustomerRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerManagementApplicationService {

    private final CustomerRegistrationService customerRegistration;
    private final Customers customers;

    @Transactional
    public CustomerId create(CustomerInput input) {
        Objects.requireNonNull(input, "Customer input is required");
        AddressData address = input.getAddress();

        Customer customer = customerRegistration.register(
                new FullName(input.getFirstName(), input.getLastName()),
                new BirthDate(input.getBirthDate()),
                new Email(input.getEmail()),
                new Phone(input.getPhone()),
                new Document(input.getDocument()),
                input.getPromotionNotificationsAllowed(),
                toAddress(address)
        );
        customers.add(customer);

        return customer.id();
    }

    @Transactional
    public void update(UUID rawCustomerId, CustomerUpdateInput input) {
        Objects.requireNonNull(input);
        Objects.requireNonNull(rawCustomerId);

        Customer customer = customers.ofId(new CustomerId(rawCustomerId))
                .orElseThrow(() -> new CustomerNotFoundException(new CustomerId(rawCustomerId)));

        customer.changeName(new FullName(input.getFirstName(), input.getLastName()));
        customer.changePhone(new Phone(input.getPhone()));
        customer.changeEmail(new Email(input.getEmail()));

        if (Boolean.TRUE.equals(input.getPromotionNotificationsAllowed())) {
            customer.enablePromotionNotifications();
        } else {
            customer.disablePromotionNotifications();
        }

        AddressData address = input.getAddress();

        customer.changeAddress(Address.builder()
                .zipCode(new ZipCode(address.getZipCode()))
                .state(address.getState())
                .city(address.getCity())
                .neighborhood(address.getNeighborhood())
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .build());

        customers.add(customer);
    }

    private Address toAddress(AddressData address) {
        Objects.requireNonNull(address, "Address is required");

        return Address.builder()
                .zipCode(new ZipCode(address.getZipCode()))
                .state(address.getState())
                .city(address.getCity())
                .neighborhood(address.getNeighborhood())
                .street(address.getStreet())
                .number(address.getNumber())
                .complement(address.getComplement())
                .build();
    }

    public void archive(UUID rawCustomerId) {
        Objects.requireNonNull(rawCustomerId, "Customer ID is required");

        Customer customer = customers.ofId(new CustomerId(rawCustomerId))
                .orElseThrow(() -> new CustomerNotFoundException(new CustomerId(rawCustomerId)));

        if(customer.isArchived())
            throw new CustomerAlreadyArchivedException(new CustomerId(rawCustomerId));

        customer.archive();

        customers.add(customer);
    }

    public void changeEmail(UUID rawCustomerId, String newEmail) {
        Objects.requireNonNull(rawCustomerId, "Customer ID is required");
        Objects.requireNonNull(newEmail, "Email is required");

        Customer customer = customers.ofId(new CustomerId(rawCustomerId))
                .orElseThrow(() -> new CustomerNotFoundException(new CustomerId(rawCustomerId)));

        if(customer.isArchived()) {
            throw new CustomerAlreadyArchivedException(new CustomerId(rawCustomerId));
        }

        customerRegistration.changeEmail(customer, newEmail);

        customers.add(customer);
    }
}
