package com.henio.algashop.ordering.presentation;

import com.henio.algashop.ordering.application.commons.AddressData;
import com.henio.algashop.ordering.application.customer.management.CustomerInput;
import com.henio.algashop.ordering.application.customer.query.CustomerOutput;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerOutput create(@RequestBody CustomerInput input) {
        AddressData address = input.getAddress();

        return CustomerOutput.builder()
                .id(new CustomerId().toString())
                .firstName(input.getFirstName())
                .lastName(input.getLastName())
                .email(input.getEmail())
                .phone(input.getPhone())
                .document(input.getDocument())
                .birthDate(input.getBirthDate())
                .promotionNotificationsAllowed(input.getPromotionNotificationsAllowed())
                .registeredAt(OffsetDateTime.now())
                .archived(false)
                .loyaltyPoints(0)
                .address(address)
                .build();
    }




}
