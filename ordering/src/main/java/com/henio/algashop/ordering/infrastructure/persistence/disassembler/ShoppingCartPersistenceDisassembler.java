package com.henio.algashop.ordering.infrastructure.persistence.disassembler;

import com.henio.algashop.ordering.domain.model.entity.ShoppingCart;
import com.henio.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.henio.algashop.ordering.domain.model.valueobject.Money;
import com.henio.algashop.ordering.domain.model.valueobject.ProductName;
import com.henio.algashop.ordering.domain.model.valueobject.Quantity;
import com.henio.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.henio.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.henio.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.henio.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import com.henio.algashop.ordering.infrastructure.persistence.entity.ShoppingCartItemPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import io.hypersistence.tsid.TSID;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ShoppingCartPersistenceDisassembler {
    public ShoppingCart toDomainEntity(ShoppingCartPersistenceEntity source) {
        return ShoppingCart.existing()
                .id(new ShoppingCartId(TSID.from(source.getId())))
                .customerId(new CustomerId(TSID.from(source.getCustomerId())))
                .totalAmount(new Money(source.getTotalAmount()))
                .createdAt(source.getCreatedAt())
                .items(toItemsDomainEntities(source.getItems()))
                .build();
    }

    private Set<ShoppingCartItem> toItemsDomainEntities(Set<ShoppingCartItemPersistenceEntity> source) {
        return source.stream().map(this::toItemEntity).collect(Collectors.toSet());
    }

    private ShoppingCartItem toItemEntity(ShoppingCartItemPersistenceEntity source) {
        return ShoppingCartItem.existing()
                .id(new ShoppingCartItemId(TSID.from(source.getId())))
                .shoppingCartId(new ShoppingCartId(TSID.from(source.getShoppingCartId())))
                .productId(new ProductId(TSID.from(source.getProductId())))
                .productName(new ProductName(source.getProductName()))
                .price(new Money(source.getPrice()))
                .quantity(new Quantity(source.getQuantity()))
                .available(source.isAvailable())
                .totalAmount(new Money(source.getTotalAmount()))
                .build();
    }
}
