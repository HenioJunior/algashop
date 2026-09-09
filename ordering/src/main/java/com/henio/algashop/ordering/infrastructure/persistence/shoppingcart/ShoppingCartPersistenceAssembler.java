package com.henio.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCart;
import com.henio.algashop.ordering.domain.model.shoppingcart.ShoppingCartItem;
import com.henio.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShoppingCartPersistenceAssembler {

    private final CustomerPersistenceEntityRepository customerPersistenceEntityRepository;

    public ShoppingCartPersistenceEntity fromDomain(ShoppingCart shoppingCart) {
        return ShoppingCartPersistenceEntity.builder()
                .id(shoppingCart.id().value().toLong())
                .customer(getCustomerById(shoppingCart))
                .totalAmount(shoppingCart.totalAmount().value())
                .totalItems(shoppingCart.totalItems().value())
                .createdAt(shoppingCart.createdAt())
                .items(toOrderItemsEntities(shoppingCart.items()))
                .build();
    }

    public ShoppingCartPersistenceEntity merge(ShoppingCartPersistenceEntity persistenceEntity,
                                               ShoppingCart shoppingCart) {
        persistenceEntity.setId(shoppingCart.id().value().toLong());
        persistenceEntity.setCustomer(getCustomerById(shoppingCart));
        persistenceEntity.setTotalAmount(shoppingCart.totalAmount().value());
        persistenceEntity.setCreatedAt(shoppingCart.createdAt());
        persistenceEntity.setItems(toOrderItemsEntities(shoppingCart.items()));
        return persistenceEntity;
    }

    private CustomerPersistenceEntity getCustomerById(ShoppingCart shoppingCart) {
        return customerPersistenceEntityRepository.getReferenceById(shoppingCart.customerId().value().toLong());
    }

    private Set<ShoppingCartItemPersistenceEntity> toOrderItemsEntities(Set<ShoppingCartItem> source) {
        return source.stream().map(i -> this.mergeItem(new ShoppingCartItemPersistenceEntity(), i)).collect(Collectors.toSet());
    }

    private ShoppingCartItemPersistenceEntity mergeItem(ShoppingCartItemPersistenceEntity persistenceEntity, ShoppingCartItem shoppingCartItem
    ) {
        persistenceEntity.setId(shoppingCartItem.id().value().toLong());
        persistenceEntity.setProductId(shoppingCartItem.productId().value().toLong());
        persistenceEntity.setProductName(shoppingCartItem.name().value());
        persistenceEntity.setPrice(shoppingCartItem.price().value());
        persistenceEntity.setQuantity(shoppingCartItem.quantity().value());
        persistenceEntity.setAvailable(shoppingCartItem.isAvailable());
        persistenceEntity.setTotalAmount(shoppingCartItem.totalAmount().value());
        return persistenceEntity;
    }

    private ShoppingCartItemPersistenceEntity toOrderItemsEntities(ShoppingCartItem source) {
        return ShoppingCartItemPersistenceEntity.builder()
                .id(source.id().value().toLong())
                .shoppingCart(ShoppingCartPersistenceEntity.builder().id(source.shoppingCartId().value().toLong()).build())
                .productId(source.productId().value().toLong())
                .productName(source.name().value())
                .price(source.price().value())
                .quantity(source.quantity().value())
                .available(source.isAvailable())
                .totalAmount(source.totalAmount().value())
                .build();
    }

}
