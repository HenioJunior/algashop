package com.henio.algashop.ordering.domain.model.customer;

import com.henio.algashop.ordering.domain.model.commons.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Transactional
class CustomerRegistrationServiceIT {

    @Autowired
    private CustomerRegistrationService customerRegistrationService;

    @Test
    void shouldRegisterCustomer() {
        Customer customer = customerRegistrationService.register(
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(1991, 7, 5)),
                new Email("johndoe@email.com"),
                new Phone("478-256-2604"),
                new Document("255-08-0578"),
                true,
                Address.builder()
                        .street("Bourbon Street")
                        .number("1134")
                        .neighborhood("North Ville")
                        .city("Yostfort")
                        .state("South Carolina")
                        .zipCode(new ZipCode("70283"))
                        .complement("Apt. 901")
                        .build()
        );

        assertThat(customer.id()).isNotNull();
        assertThat(customer.fullName()).isEqualTo(new FullName("John", "Doe"));
        assertThat(customer.email()).isEqualTo(new Email("johndoe@email.com"));
        assertThat(customer.birthDate()).isEqualTo(new BirthDate(LocalDate.of(1991, 7, 5)));
        assertThat(customer.phone()).isEqualTo(new Phone("478-256-2604"));
        assertThat(customer.document()).isEqualTo(new Document("255-08-0578"));
        assertThat(customer.isPromotionNotificationsAllowed()).isTrue();
        assertThat(customer.isArchived()).isFalse();
        assertThat(customer.archivedAt()).isNull();
        assertThat(customer.loyaltyPoints()).isEqualTo(LoyaltyPoints.ZERO);
        assertThat(customer.registeredAt()).isNotNull();
        }
}
