package com.henio.algashop.ordering.infrastructure.persistence.assembler;

import com.henio.algashop.ordering.domain.model.entity.Customer;
import com.henio.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.henio.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerPersistenceAssemblerTest {

    private CustomerPersistenceAssembler assembler = new CustomerPersistenceAssembler();

    @Test
    void shouldConvertFromDomain() {
        Customer newCustomer = CustomerTestDataBuilder.brandNewCustomer();
        CustomerPersistenceEntity customerPersistenceEntity = assembler.fromDomain(newCustomer);

        assertThat(customerPersistenceEntity).satisfies(
                c -> assertThat(c.getId()).isEqualTo(newCustomer.id().value().toLong()),
                c -> assertThat(c.getFirstName()).isEqualTo(newCustomer.fullName().firstName()),
                c -> assertThat(c.getLastName()).isEqualTo(newCustomer.fullName().lastName()),
                c -> assertThat(c.getBirthDate()).isEqualTo(newCustomer.birthDate().toString()),
                c -> assertThat(c.getEmail()).isEqualTo(newCustomer.email().toString()),
                c -> assertThat(c.getPhone()).isEqualTo(newCustomer.phone().toString()),
                c -> assertThat(c.getDocument()).isEqualTo(newCustomer.document().toString()),
                c -> assertThat(c.isPromotionNotificationsAllowed()).isTrue(),
                c -> assertThat(c.isArchived()).isFalse(),
                c -> assertThat(c.getRegisteredAt()).isEqualTo(newCustomer.registeredAt()),
                c -> assertThat(c.getArchivedAt()).isEqualTo(newCustomer.archivedAt()),
                c -> assertThat(c.getLoyaltyPoints()).isEqualTo(newCustomer.loyaltyPoints().value())
        );
    }
}