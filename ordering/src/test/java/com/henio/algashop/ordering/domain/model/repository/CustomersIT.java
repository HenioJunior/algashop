package com.henio.algashop.ordering.domain.model.repository;


import com.henio.algashop.ordering.domain.model.entity.Customer;
import com.henio.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.henio.algashop.ordering.domain.model.valueobject.Email;
import com.henio.algashop.ordering.domain.model.valueobject.FullName;
import com.henio.algashop.ordering.infrastructure.persistence.adapter.CustomersPersistenceAdapter;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.repository.CustomerPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@DataJpaTest
@Import({
        CustomersPersistenceAdapter.class,
        CustomerPersistenceAssembler.class,
        CustomerPersistenceDisassembler.class
})
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
        newTransaction.executeWithoutResult(status -> repository.deleteAll());
    }

    @Test
    void shouldPersistAndFindCustomer() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer();

        customers.add(customer);

        Customer persistedCustomer = customers.ofId(customer.id())
                .orElseThrow();

        Assertions.assertThat(persistedCustomer.id())
                .isEqualTo(customer.id());

        Assertions.assertThat(persistedCustomer.fullName())
                .isEqualTo(customer.fullName());

        Assertions.assertThat(persistedCustomer.birthDate())
                .isEqualTo(customer.birthDate());

        Assertions.assertThat(persistedCustomer.email())
                .isEqualTo(customer.email());

        Assertions.assertThat(persistedCustomer.phone())
                .isEqualTo(customer.phone());

        Assertions.assertThat(persistedCustomer.document())
                .isEqualTo(customer.document());

        Assertions.assertThat(persistedCustomer.isPromotionNotificationsAllowed())
                .isEqualTo(customer.isPromotionNotificationsAllowed());

        Assertions.assertThat(persistedCustomer.loyaltyPoints())
                .isEqualTo(customer.loyaltyPoints());

        Assertions.assertThat(persistedCustomer.address())
                .isEqualTo(customer.address());

        Assertions.assertThat(persistedCustomer.registeredAt())
                .isEqualTo(customer.registeredAt());

        Assertions.assertThat(persistedCustomer.version())
                .isNotNull();
    }

    @Test
    void shouldCheckIfCustomerExists() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer();

        Assertions.assertThat(customers.exists(customer.id()))
                .isFalse();

        customers.add(customer);

        Assertions.assertThat(customers.exists(customer.id()))
                .isTrue();
    }

    @Test
    void shouldCountExistingCustomers() {
        Assertions.assertThat(customers.count())
                .isZero();

        Customer customer1 = CustomerTestDataBuilder.brandNewCustomer();
        Customer customer2 = CustomerTestDataBuilder.brandNewCustomer();

        customers.add(customer1);
        customers.add(customer2);

        Assertions.assertThat(customers.count())
                .isEqualTo(2L);
    }

    @Test
    void shouldUpdateCustomerAndIncrementVersion() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer();

        customers.add(customer);

        Long previousVersion = customer.version();

        customer.changeEmail(new Email("new@email.com"));

        customers.add(customer);

        Assertions.assertThat(customer.version())
                .isGreaterThan(previousVersion);

        Customer updatedCustomer = customers.ofId(customer.id())
                .orElseThrow();

        Assertions.assertThat(updatedCustomer.email())
                .isEqualTo(new Email("new@email.com"));

        Assertions.assertThat(updatedCustomer.version())
                .isEqualTo(customer.version());
    }

    @Test
    void shouldNotAllowStaleUpdates() {
        Customer customer = CustomerTestDataBuilder.brandNewCustomer();

        newTransaction.executeWithoutResult(status ->
                customers.add(customer)
        );

        Customer customerT1 = newTransaction.execute(status ->
                customers.ofId(customer.id()).orElseThrow()
        );

        Customer customerT2 = newTransaction.execute(status ->
                customers.ofId(customer.id()).orElseThrow()
        );

        newTransaction.executeWithoutResult(status -> {
            customerT1.changeEmail(new Email("customer1@email.com"));
            customers.add(customerT1);
        });

        Assertions.assertThatExceptionOfType(
                ObjectOptimisticLockingFailureException.class
        ).isThrownBy(() ->
                newTransaction.executeWithoutResult(status -> {
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

        Assertions.assertThat(archivedCustomer.isArchived()).isTrue();
        Assertions.assertThat(archivedCustomer.archivedAt()).isNotNull();

        Assertions.assertThat(archivedCustomer.fullName())
                .isEqualTo(new FullName("Anonymous", "Customer"));

        Assertions.assertThat(archivedCustomer.birthDate()).isNull();
        Assertions.assertThat(archivedCustomer.phone()).isNull();
        Assertions.assertThat(archivedCustomer.document()).isNull();

        Assertions.assertThat(archivedCustomer.isPromotionNotificationsAllowed())
                .isFalse();
    }
}
