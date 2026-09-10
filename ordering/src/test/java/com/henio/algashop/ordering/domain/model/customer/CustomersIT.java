package com.henio.algashop.ordering.domain.model.customer;


import com.henio.algashop.ordering.domain.model.commons.Email;
import com.henio.algashop.ordering.domain.model.commons.FullName;
import com.henio.algashop.ordering.infrastructure.persistence.customer.CustomersPersistenceAdapter;
import com.henio.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({
        CustomersPersistenceAdapter.class,
        CustomerPersistenceAssembler.class,
        CustomerPersistenceDisassembler.class
})
@Transactional
public class CustomersIT {

    private final Customers customers;
    private final TransactionTemplate newTransaction;
    private final CustomerPersistenceEntityRepository repository;

    @Autowired
    CustomersIT(
            Customers customers,
            PlatformTransactionManager transactionManager,
            CustomerPersistenceEntityRepository repository
    ) {
        this.customers = customers;

        this.newTransaction = new TransactionTemplate(transactionManager);
        this.newTransaction.setPropagationBehavior(
                TransactionDefinition.PROPAGATION_REQUIRES_NEW
        );
        this.repository = repository;
    }

    @BeforeEach
    void cleanup() {
        newTransaction.executeWithoutResult(_ -> repository.deleteAll());
    }

    @Test
    void shouldPersistAndFindCustomer() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer();

        customers.add(customer);

        Customer persistedCustomer = customers.ofId(customer.id())
                .orElseThrow();

        assertThat(persistedCustomer.id())
                .isEqualTo(customer.id());

        assertThat(persistedCustomer.fullName())
                .isEqualTo(customer.fullName());

        assertThat(persistedCustomer.birthDate())
                .isEqualTo(customer.birthDate());

        assertThat(persistedCustomer.email())
                .isEqualTo(customer.email());

        assertThat(persistedCustomer.phone())
                .isEqualTo(customer.phone());

        assertThat(persistedCustomer.document())
                .isEqualTo(customer.document());

        assertThat(persistedCustomer.isPromotionNotificationsAllowed())
                .isEqualTo(customer.isPromotionNotificationsAllowed());

        assertThat(persistedCustomer.loyaltyPoints())
                .isEqualTo(customer.loyaltyPoints());

        assertThat(persistedCustomer.address())
                .isEqualTo(customer.address());

        assertThat(persistedCustomer.registeredAt())
                .isEqualTo(customer.registeredAt());

        assertThat(persistedCustomer.version())
                .isNotNull();
    }

    @Test
    void shouldCheckIfCustomerExists() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer();

        assertThat(customers.exists(customer.id()))
                .isFalse();

        customers.add(customer);

        assertThat(customers.exists(customer.id()))
                .isTrue();
    }

    @Test
    void shouldCountExistingCustomers() {
        assertThat(customers.count())
                .isZero();

        Customer customer1 = CustomerTestDataBuilder.brandNewCustomer();
        Customer customer2 = CustomerTestDataBuilder.brandNewCustomer();

        customers.add(customer1);
        customers.add(customer2);

        assertThat(customers.count())
                .isEqualTo(2L);
    }

    @Test
    void shouldUpdateCustomerAndIncrementVersion() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer();

        customers.add(customer);

        Long previousVersion = customer.version();

        customer.changeEmail(new Email("new@email.com"));

        customers.add(customer);

        assertThat(customer.version())
                .isGreaterThan(previousVersion);

        Customer updatedCustomer = customers.ofId(customer.id())
                .orElseThrow();

        assertThat(updatedCustomer.email())
                .isEqualTo(new Email("new@email.com"));

        assertThat(updatedCustomer.version())
                .isEqualTo(customer.version());
    }

    @Test
    void shouldNotAllowStaleUpdates() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer();

        newTransaction.executeWithoutResult(_ ->
                customers.add(customer)
        );

        Customer customerT1 = newTransaction.execute(_ ->
                customers.ofId(customer.id()).orElseThrow()
        );

        Customer customerT2 = newTransaction.execute(_ ->
                customers.ofId(customer.id()).orElseThrow()
        );

        newTransaction.executeWithoutResult(_ -> {
            customerT1.changeEmail(new Email("customer1@email.com"));
            customers.add(customerT1);
        });

        Assertions.assertThatExceptionOfType(
                ObjectOptimisticLockingFailureException.class
        ).isThrownBy(() ->
                newTransaction.executeWithoutResult(_ -> {
                    customerT2.changeEmail(new Email("customer2@email.com"));
                    customers.add(customerT2);
                })
        );
    }

    @Test
    void shouldPersistArchivedCustomer() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer();

        customers.add(customer);

        customer.archive();
        customers.add(customer);

        Customer archivedCustomer = customers.ofId(customer.id())
                .orElseThrow();

        assertThat(archivedCustomer.isArchived()).isTrue();
        assertThat(archivedCustomer.archivedAt()).isNotNull();

        assertThat(archivedCustomer.fullName())
                .isEqualTo(new FullName("Anonymous", "Customer"));

        assertThat(archivedCustomer.birthDate()).isNull();
        assertThat(archivedCustomer.phone()).isNull();
        assertThat(archivedCustomer.document()).isNull();

        assertThat(archivedCustomer.isPromotionNotificationsAllowed())
                .isFalse();
    }

    @Test
    void shouldFindByEmail() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer();
        customers.add(customer);

        Optional<Customer> customerOptional = customers.ofEmail(customer.email());

        assertThat(customerOptional).isPresent();

    }

    @Test
    void shouldNotFindByEmailIfNoCustomerExistsWithEmail() {
        Optional<Customer> customerOptional = customers.ofEmail(new Email(UUID.randomUUID() + "@email.com"));
        assertThat(customerOptional).isNotPresent();

    }

    @Test
    void shouldCheckEmailUniqueness() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer();
        customers.add(customer);

        assertThat(customers.isEmailUnique(customer.email()))
                .isFalse();

        assertThat(customers.isEmailUnique(new Email("alex@gmail.com")))
                .isTrue();
    }
}
