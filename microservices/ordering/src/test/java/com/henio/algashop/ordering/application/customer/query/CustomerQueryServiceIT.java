package com.henio.algashop.ordering.application.customer.query;

import com.henio.algashop.ordering.domain.model.commons.Email;
import com.henio.algashop.ordering.domain.model.commons.FullName;
import com.henio.algashop.ordering.domain.model.customer.Customer;
import com.henio.algashop.ordering.domain.model.customer.CustomerId;
import com.henio.algashop.ordering.domain.model.customer.CustomerTestDataBuilder;
import com.henio.algashop.ordering.domain.model.customer.Customers;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CustomerQueryServiceIT {

    @Autowired
    private CustomerQueryService queryService;

    @Autowired
    private Customers customers;

    @Test
    public void shouldFindById() {
        Customer customer = CustomerTestDataBuilder.existingCustomer().build();
        customers.add(customer);

        CustomerOutput output = queryService.findById(customer.id().value().toString());

        assertThat(output)
                .extracting(
                        CustomerOutput::getId,
                        CustomerOutput::getFirstName,
                        CustomerOutput::getEmail
                ).containsExactly(
                        customer.id().value().toString(),
                        customer.fullName().firstName(),
                        customer.email().value()
                );
    }

    @Test
    public void shouldFilterByPage() {
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Ana", "Silva")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Bruno", "Costa")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Carla", "Souza")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Daniel", "Pereira")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Eduarda", "Santos")).build());

        CustomerFilter filter = new CustomerFilter(0, 2);
        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumberOfElements()).isEqualTo(2);
    }

    @Test
    public void shouldFilterByFirstName() {
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Alice", "Smith")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Bob", "Johnson")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Alice", "Williams")).build());

        CustomerFilter filter = new CustomerFilter();
        filter.setFirstName("alice");

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getFirstName)
                .containsOnly("Alice");
    }

    @Test
    public void shouldFilterByEmail() {
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).email(new Email("user1@test.com")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).email(new Email("test2@algashop.com")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).email(new Email("user3@test.com")).build());

        CustomerFilter filter = new CustomerFilter();
        filter.setEmail("test");

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.getTotalElements()).isEqualTo(3);
        assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getEmail)
                .containsExactlyInAnyOrder("user1@test.com", "test2@algashop.com", "user3@test.com");
    }

    @Test
    public void shouldFilterByMultipleParams() {
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("John", "Doe")).email(new Email("johndoe@email.com")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Jane", "Doe")).email(new Email("janedoe@email.com")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("John", "Smith")).email(new Email("johnsmith@email.com")).build());

        CustomerFilter filter = new CustomerFilter();
        filter.setFirstName("john");
        filter.setEmail("doe");

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getContent().getFirst().getFirstName()).isEqualTo("John");
        assertThat(page.getContent().getFirst().getEmail()).isEqualTo("johndoe@email.com");
    }

    @Test
    public void shouldOrderByFirstNameAsc() {
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Zoe", "Doe")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Charlie", "Smith")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Alice", "Williams")).build());

        CustomerFilter filter = new CustomerFilter();
        filter.setSortByProperty(CustomerFilter.SortType.FIRST_NAME);
        filter.setSortDirection(Sort.Direction.ASC);

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.getContent().getFirst().getFirstName()).isEqualTo("Alice");
    }

    @Test
    public void shouldOrderByFirstNameDesc() {
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Zoe", "Doe")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Charlie", "Smith")).build());
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("Alice", "Williams")).build());

        CustomerFilter filter = new CustomerFilter();
        filter.setSortByProperty(CustomerFilter.SortType.FIRST_NAME);
        filter.setSortDirection(Sort.Direction.DESC);

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.getContent().getFirst().getFirstName()).isEqualTo("Zoe");
    }

    @Test
    public void givenNonMatchingFilter_shouldReturnEmptyPage() {
        customers.add(CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).fullName(new FullName("John", "Doe")).build());

        CustomerFilter filter = new CustomerFilter();
        filter.setFirstName("NonExistent");

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.isEmpty()).isTrue();
        assertThat(page.getTotalElements()).isEqualTo(0);
    }
}