package com.henio.algashop.ordering.infrastructure.persistence.customer;

import com.henio.algashop.ordering.application.customer.query.CustomerFilter;
import com.henio.algashop.ordering.application.customer.query.CustomerQueryService;
import com.henio.algashop.ordering.application.customer.query.CustomerSummaryOutput;
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

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

@SpringBootTest
@Transactional
class CustomerQueryServiceIT {

    @Autowired
    private CustomerQueryService queryService;

    @Autowired
    private Customers customers;

    @Test
    public void shouldFilterByFirstNameAndEmail() {
        Customer john = CustomerTestDataBuilder.existingCustomer()
                .fullName(new FullName("John", "Doe"))
                .email(new Email("john@gmail.com"))
                .build();

        Customer maria = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Maria", "Smith"))
                .email(new Email("maria@gmail.com"))
                .build();

        Customer robert = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Robert", "Cray"))
                .email(new Email("john@yahoo.com"))
                .build();

        Customer bob = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Bob", "Green"))
                .email(new Email("bob@gmail.com"))
                .build();

        Customer geddy = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Geddy", "Lee"))
                .email(new Email("geddy@gmail.com"))
                .build();

        customers.add(john);
        customers.add(maria);
        customers.add(robert);
        customers.add(bob);
        customers.add(geddy);

        CustomerFilter filter = new CustomerFilter();
        filter.setFirstName("JOHN");
        filter.setEmail("gmail");

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.getNumberOfElements()).isEqualTo(1);
        assertThat(page.getTotalElements()).isEqualTo(1);
        assertThat(page.getTotalPages()).isEqualTo(1);

        assertThat(page.getContent())
                .extracting(
                        CustomerSummaryOutput::getFirstName,
                        CustomerSummaryOutput::getEmail
                )
                .containsExactly(
                        tuple("John", "john@gmail.com")
                );
    }

    @Test
    public void shouldFilterByFirstName() {
        Customer john = CustomerTestDataBuilder.existingCustomer()
                .fullName(new FullName("John", "Doe"))
                .build();

        Customer maria = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Maria", "Smith"))
                .build();
        customers.add(john);
        customers.add(maria);

        CustomerFilter filter = new CustomerFilter();
        filter.setFirstName("OH");

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.getTotalElements()).isEqualTo(1);

        assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getFirstName)
                .containsExactly("John");
    }

    @Test
    public void shouldFilterByEmail() {
        Customer john = CustomerTestDataBuilder.existingCustomer()
                .fullName(new FullName("John", "Doe"))
                .build();

        Customer maria = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Maria", "Smith"))
                .email(new Email("maria.smith@example.com"))
                .build();
        customers.add(john);
        customers.add(maria);

        CustomerFilter filter = new CustomerFilter();
        filter.setEmail("OH");

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.getTotalElements()).isEqualTo(1);

        assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getFirstName)
                .containsExactly("John");

    }

    @Test
    public void shouldPaginateCustomers() {
        Customer john = CustomerTestDataBuilder.existingCustomer()
                .fullName(new FullName("John", "Doe"))
                .email(new Email("john@gmail.com"))
                .build();

        Customer maria = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Maria", "Smith"))
                .email(new Email("maria@gmail.com"))
                .build();

        Customer robert = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Robert", "Cray"))
                .email(new Email("john@yahoo.com"))
                .build();

        Customer bob = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Bob", "Green"))
                .email(new Email("bob@gmail.com"))
                .build();

        Customer geddy = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Geddy", "Lee"))
                .email(new Email("geddy@gmail.com"))
                .build();

        customers.add(john);
        customers.add(maria);
        customers.add(robert);
        customers.add(bob);
        customers.add(geddy);

        CustomerFilter filter = new CustomerFilter(1, 2);

        Page<CustomerSummaryOutput> page = queryService.filter(filter);


        assertThat(page.getNumber()).isEqualTo(1);
        assertThat(page.getSize()).isEqualTo(2);
        assertThat(page.getNumberOfElements()).isEqualTo(2);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(3);

    }

    @Test
    public void shouldSortByFirstNameAscending() {
        Customer john = CustomerTestDataBuilder.existingCustomer()
                .fullName(new FullName("John", "Doe"))
                .email(new Email("john@gmail.com"))
                .build();

        Customer maria = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Maria", "Smith"))
                .email(new Email("maria@gmail.com"))
                .build();

        Customer robert = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Robert", "Cray"))
                .email(new Email("john@yahoo.com"))
                .build();

        Customer bob = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Bob", "Green"))
                .email(new Email("bob@gmail.com"))
                .build();

        Customer geddy = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Geddy", "Lee"))
                .email(new Email("geddy@gmail.com"))
                .build();

        customers.add(john);
        customers.add(maria);
        customers.add(robert);
        customers.add(bob);
        customers.add(geddy);

        CustomerFilter filter = new CustomerFilter();

        filter.setSortByProperty(CustomerFilter.SortType.FIRST_NAME);
        filter.setSortDirection(Sort.Direction.ASC);

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getFirstName)
                .containsExactly("Bob", "Geddy", "John", "Maria", "Robert");
    }

    @Test
    public void shouldSortByFirstNameDescending() {
        Customer john = CustomerTestDataBuilder.existingCustomer()
                .fullName(new FullName("John", "Doe"))
                .email(new Email("john@gmail.com"))
                .build();

        Customer maria = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Maria", "Smith"))
                .email(new Email("maria@gmail.com"))
                .build();

        Customer robert = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Robert", "Cray"))
                .email(new Email("john@yahoo.com"))
                .build();

        customers.add(john);
        customers.add(maria);
        customers.add(robert);

        CustomerFilter filter = new CustomerFilter();

        filter.setSortByProperty(CustomerFilter.SortType.FIRST_NAME);
        filter.setSortDirection(Sort.Direction.DESC);

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getFirstName)
                .containsExactly("Robert", "Maria", "John");
    }

    @Test
    public void shouldSortByRegisteredAtAscending() {
        OffsetDateTime firstDate  = OffsetDateTime.parse("2026-01-01T10:00:00-03:00");
        OffsetDateTime secondDate = OffsetDateTime.parse("2026-02-01T10:00:00-03:00");
        OffsetDateTime thirdDate  = OffsetDateTime.parse("2026-03-01T10:00:00-03:00");

        Customer john = CustomerTestDataBuilder.existingCustomer()
                .fullName(new FullName("John", "Doe"))
                .email(new Email("john@gmail.com"))
                .registeredAt(firstDate)
                .build();

        Customer maria = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Maria", "Smith"))
                .email(new Email("maria@gmail.com"))
                .registeredAt(secondDate)
                .build();

        Customer robert = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Robert", "Cray"))
                .email(new Email("john@yahoo.com"))
                .registeredAt(thirdDate)
                .build();

        customers.add(john);
        customers.add(maria);
        customers.add(robert);

        CustomerFilter filter = new CustomerFilter();

        filter.setSortByProperty(null);
        filter.setSortDirection(Sort.Direction.ASC);

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getFirstName)
                .containsExactly("John", "Maria", "Robert");
    }

    @Test
    public void shouldSortByRegisteredAtDescending() {
        OffsetDateTime firstDate  = OffsetDateTime.parse("2026-01-01T10:00:00-03:00");
        OffsetDateTime secondDate = OffsetDateTime.parse("2026-02-01T10:00:00-03:00");
        OffsetDateTime thirdDate  = OffsetDateTime.parse("2026-03-01T10:00:00-03:00");

        Customer john = CustomerTestDataBuilder.existingCustomer()
                .fullName(new FullName("John", "Doe"))
                .email(new Email("john@gmail.com"))
                .registeredAt(thirdDate)
                .build();

        Customer maria = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Maria", "Smith"))
                .email(new Email("maria@gmail.com"))
                .registeredAt(secondDate)
                .build();

        Customer robert = CustomerTestDataBuilder.existingCustomer()
                .id(new CustomerId())
                .fullName(new FullName("Robert", "Cray"))
                .email(new Email("john@yahoo.com"))
                .registeredAt(firstDate)
                .build();

        customers.add(john);
        customers.add(maria);
        customers.add(robert);

        CustomerFilter filter = new CustomerFilter();

        filter.setSortByProperty(null);
        filter.setSortDirection(Sort.Direction.DESC);

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.getContent())
                .extracting(CustomerSummaryOutput::getFirstName)
                .containsExactly("John", "Maria", "Robert");
    }

    @Test
    void shouldReturnEmptyPageWhenNoCustomerMatches() {
        Customer john = CustomerTestDataBuilder.existingCustomer()
                .fullName(new FullName("John", "Doe"))
                .email(new Email("john@gmail.com"))
                .build();

        customers.add(john);

        CustomerFilter filter = new CustomerFilter();
        filter.setFirstName("Maria");

        Page<CustomerSummaryOutput> page = queryService.filter(filter);

        assertThat(page.getContent()).isEmpty();
        assertThat(page.getNumberOfElements()).isZero();
        assertThat(page.getTotalElements()).isZero();
        assertThat(page.getTotalPages()).isZero();
    }
}