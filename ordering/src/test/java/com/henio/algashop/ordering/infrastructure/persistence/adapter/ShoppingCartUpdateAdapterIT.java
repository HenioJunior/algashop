package com.henio.algashop.ordering.infrastructure.persistence.adapter;

import com.henio.algashop.ordering.domain.model.entity.*;
import com.henio.algashop.ordering.domain.model.valueobject.Money;
import com.henio.algashop.ordering.domain.model.valueobject.Product;
import com.henio.algashop.ordering.domain.model.valueobject.Quantity;
import com.henio.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.assembler.ShoppingCartPersistenceAssembler;
import com.henio.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceDisassembler;
import com.henio.algashop.ordering.infrastructure.persistence.disassembler.ShoppingCartPersistenceDisassembler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import({
        ShoppingCartUpdateAdapter.class,
        ShoppingCartsPersistenceAdapter.class,
        ShoppingCartPersistenceAssembler.class,
        ShoppingCartPersistenceDisassembler.class,
        CustomersPersistenceAdapter.class,
        CustomerPersistenceAssembler.class,
        CustomerPersistenceDisassembler.class,
        SpringDataAuditingConfig.class
})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ShoppingCartUpdateAdapterIT {

    private final ShoppingCartsPersistenceAdapter persistenceAdapter;
    private final CustomersPersistenceAdapter customersPersistenceAdapter;

    private final ShoppingCartUpdateAdapter shoppingCartUpdateAdapter;

    @Autowired
    public ShoppingCartUpdateAdapterIT(ShoppingCartsPersistenceAdapter persistenceAdapter,
                                        CustomersPersistenceAdapter customersPersistenceAdapter,
                                       ShoppingCartUpdateAdapter shoppingCartUpdateAdapter) {
        this.persistenceAdapter = persistenceAdapter;
        this.customersPersistenceAdapter = customersPersistenceAdapter;
        this.shoppingCartUpdateAdapter = shoppingCartUpdateAdapter;
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
    @Transactional(propagation = Propagation.NEVER)
    void shouldUpdateItemPriceAndTotalAmount() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();

        Product product1 = ProductTestDataBuilder.aProduct().price(new Money("2000")).build();
        Product product2 = ProductTestDataBuilder.aProductAltRamMemory().price(new Money("200")).build();

        shoppingCart.addItem(product1, new Quantity(2));
        shoppingCart.addItem(product2, new Quantity(1));

        persistenceAdapter.add(shoppingCart);

        ProductId productIdToUpdate = product1.id();
        Money newProduct1Price = new Money("1500");
        Money expectedNewItemTotalPrice = newProduct1Price.multiply(new Quantity(2));
        Money expectedNewCartTotalAmount = expectedNewItemTotalPrice.add(new Money("200"));

        shoppingCartUpdateAdapter.adjustPrice(productIdToUpdate, newProduct1Price);

        ShoppingCart updatedShoppingCart = persistenceAdapter.ofId(shoppingCart.id()).orElseThrow();

        assertThat(updatedShoppingCart.totalAmount()).isEqualTo(expectedNewCartTotalAmount);
        assertThat(updatedShoppingCart.totalItems()).isEqualTo(new Quantity(3));

        ShoppingCartItem item = updatedShoppingCart.findItem(productIdToUpdate);

        assertThat(item.totalAmount()).isEqualTo(expectedNewItemTotalPrice);
        assertThat(item.price()).isEqualTo(newProduct1Price);

    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    void shouldUpdateItemAvailability() {
        ShoppingCart shoppingCart = ShoppingCartTestDataBuilder.aShoppingCart().withItems(false).build();

        Product product1 = ProductTestDataBuilder.aProduct()
                .price(new Money("2000"))
                .inStock(true).build();
        Product product2 = ProductTestDataBuilder.aProductAltRamMemory()
                .price(new Money("200"))
                .inStock(true).build();

        shoppingCart.addItem(product1, new Quantity(2));
        shoppingCart.addItem(product2, new Quantity(1));

        persistenceAdapter.add(shoppingCart);

        var productIdToUpdate = product1.id();
        var productIdNotToUpdate = product2.id();

        shoppingCartUpdateAdapter.changeAvailability(productIdToUpdate, false);

        ShoppingCart updatedShoppingCart = persistenceAdapter.ofId(shoppingCart.id()).orElseThrow();

        ShoppingCartItem item = updatedShoppingCart.findItem(productIdToUpdate);

        assertThat(item.isAvailable()).isFalse();

        ShoppingCartItem item2 = updatedShoppingCart.findItem(productIdNotToUpdate);

        assertThat(item2.isAvailable()).isTrue();

    }
}
