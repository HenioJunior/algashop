package com.henio.algashop.ordering.domain.entity;

import com.henio.algashop.ordering.domain.valueobject.Money;
import com.henio.algashop.ordering.domain.valueobject.Product;
import com.henio.algashop.ordering.domain.valueobject.ProductName;
import com.henio.algashop.ordering.domain.valueobject.Quantity;
import com.henio.algashop.ordering.domain.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ShoppingCartItemTest {
    @Test
    public void givenValidData_whenCreateNewItem_shouldInitializeCorrectly() {
        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.aShoppingCartItem()
                .productName(new ProductName("Notebook"))
                .price(new Money("2000"))
                .quantity(new Quantity(2))
                .available(true)
                .build();

        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.id()).isNotNull(),
                i -> Assertions.assertThat(i.shoppingCartId()).isNotNull(),
                i -> Assertions.assertThat(i.productId()).isNotNull(),
                i -> Assertions.assertThat(i.name()).isEqualTo(new ProductName("Notebook")),
                i -> Assertions.assertThat(i.price()).isEqualTo(new Money("2000")),
                i -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(2)),
                i -> Assertions.assertThat(i.isAvailable()).isTrue(),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("4000"))
        );
    }

    @Test
    public void givenItem_whenChangeQuantity_shouldRecalculateTotal() {
        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.aShoppingCartItem()
                .price(new Money("1000"))
                .quantity(new Quantity(1))
                .build();

        item.changeQuantity(new Quantity(3));

        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(3)),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("3000"))
        );
    }

    @Test
    public void givenItem_whenChangePrice_shouldRecalculateTotal() {
        ProductId productId = new ProductId();

        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.aShoppingCartItem()
                .productId(productId)
                .price(new Money("1500"))
                .quantity(new Quantity(2))
                .build();

        Product product = ProductTestDataBuilder.aProduct()
                .id(productId)
                .price(new Money("2000"))
                .build();

        item.refresh(product);

        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.price()).isEqualTo(new Money("2000")),
                i -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("4000"))
        );
    }

    @Test
    public void givenItem_whenChangeAvailability_shouldUpdateStatus() {
        ProductId productId = new ProductId();

        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.aShoppingCartItem()
                .productId(productId)
                .available(true)
                .build();

        Product product = ProductTestDataBuilder.aProduct()
                .id(productId)
                .inStock(false)
                .build();

        item.refresh(product);

        Assertions.assertThat(item.isAvailable()).isFalse();
    }

    @Test
    public void givenDifferentIds_whenCompareItems_shouldNotBeEqual() {
        ShoppingCartItem item1 = ShoppingCartItemTestDataBuilder.aShoppingCartItem().build();
        ShoppingCartItem item2 = ShoppingCartItemTestDataBuilder.aShoppingCartItem().build();

        Assertions.assertThat(item1).isNotEqualTo(item2);
    }
}