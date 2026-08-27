package com.henio.algashop.ordering.infrastructure.persistence.adapter;


import com.henio.algashop.ordering.domain.model.entity.Customer;
import com.henio.algashop.ordering.domain.model.entity.CustomerTestDataBuilder;
import com.henio.algashop.ordering.domain.model.entity.ShoppingCart;
import com.henio.algashop.ordering.domain.model.entity.ShoppingCartTestDataBuilder;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.ShoppingCartPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.repository.ShoppingCartPersistenceEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;

@DataJpaTest
@Import({
        ShoppingCartsPersistenceAdapter.class,
        ShoppingCartPersistenceAssembler.class,
        ShoppingCartPersistenceDisassembler.class,
        CustomersPersistenceAdapter.class,
        CustomerPersistenceAssembler.class,
        CustomerPersistenceDisassembler.class,
        SpringDataAuditingConfig.class
})
class ShoppingCartsPersistenceAdapterIT {

    private final ShoppingCartsPersistenceAdapter persistenceAdapter;
    private final CustomersPersistenceAdapter customersPersistenceAdapter;
    private final ShoppingCartPersistenceEntityRepository entityRepository;

    @Autowired
    public ShoppingCartsPersistenceAdapterIT(ShoppingCartsPersistenceAdapter persistenceAdapter, CustomersPersistenceAdapter customersPersistenceAdapter, ShoppingCartPersistenceEntityRepository entityRepository) {
        this.persistenceAdapter = persistenceAdapter;
        this.customersPersistenceAdapter = customersPersistenceAdapter;
        this.entityRepository = entityRepository;
    }

    @BeforeEach
    public void setup() {
        if (!customersPersistenceAdapter.exists(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)) {
            customersPersistenceAdapter.add(
                    CustomerTestDataBuilder.existingCustomer().build()
            );
        }
    }

    @Test
    public void shouldAddAndFindShoppingCart() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        assertThat(shoppingCart.version()).isNull();

        persistenceAdapter.add(shoppingCart);

        assertThat(shoppingCart.version()).isNotNull().isEqualTo(0L);

        ShoppingCart foundCart = persistenceAdapter.ofId(shoppingCart.id()).orElseThrow();
        assertThat(foundCart).isNotNull();
        assertThat(foundCart.id()).isEqualTo(shoppingCart.id());
        assertThat(foundCart.totalItems().value()).isEqualTo(3);
    }

    @Test
    public void shouldRemoveShoppingCartById() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        persistenceAdapter.add(shoppingCart);
        assertThat(persistenceAdapter.exists(shoppingCart.id())).isTrue();

        persistenceAdapter.remove(shoppingCart.id());

        assertThat(persistenceAdapter.exists(shoppingCart.id())).isFalse();
        assertThat(entityRepository.findById(shoppingCart.id().value().toLong())).isEmpty();
    }

    @Test
    public void shouldRemoveShoppingCartByEntity() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();
        persistenceAdapter.add(shoppingCart);
        assertThat(persistenceAdapter.exists(shoppingCart.id())).isTrue();

        persistenceAdapter.remove(shoppingCart);

        assertThat(persistenceAdapter.exists(shoppingCart.id())).isFalse();
    }

    @Test
    public void shouldFindShoppingCartByCustomerId() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart()
                .customerId(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID)
                .build();
        persistenceAdapter.add(shoppingCart);

        ShoppingCart foundCart = persistenceAdapter.ofCustomer(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID).orElseThrow();

        assertThat(foundCart).isNotNull();
        assertThat(foundCart.customerId()).isEqualTo(CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID);
        assertThat(foundCart.id()).isEqualTo(shoppingCart.id());
    }

    @Test
    public void shouldCorrectlyCountShoppingCarts() {
        long initialCount = persistenceAdapter.count();

        ShoppingCart cart1 = ShoppingCartTestDataBuilder.aShoppingCart().build();
        persistenceAdapter.add(cart1);

        Customer otherCustomer = CustomerTestDataBuilder.existingCustomer().id(new CustomerId()).build();
        customersPersistenceAdapter.add(otherCustomer);

        ShoppingCart cart2 = ShoppingCartTestDataBuilder.aShoppingCart().customerId(otherCustomer.id()).build();
        persistenceAdapter.add(cart2);

        long finalCount = persistenceAdapter.count();

        assertThat(finalCount).isEqualTo(initialCount + 2);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void shouldAddAndFindWhenNoTransaction() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().build();

        persistenceAdapter.add(shoppingCart);

        assertThatNoException().isThrownBy(() -> {
            ShoppingCart foundCart = persistenceAdapter.ofId(shoppingCart.id()).orElseThrow();
            assertThat(foundCart).isNotNull();
        });
    }
    
}