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
        ShoppingCartPersistenceEntity entity = ShoppingCartPersistenceEntity.builder()
                .id(shoppingCart.id().value().toLong())
                .customer(getCustomerById(shoppingCart))
                .totalAmount(shoppingCart.totalAmount().value())
                .totalItems(shoppingCart.totalItems().value())
                .createdAt(shoppingCart.createdAt())
                .version(shoppingCart.version())
                .build();

        entity.addItem(toItemsEntities(shoppingCart.items()));
        entity.addEvents(shoppingCart.domainEvents());

        return entity;
    }

    public void merge(ShoppingCartPersistenceEntity entity,
                      ShoppingCart shoppingCart) {
        entity.setId(shoppingCart.id().value().toLong());
        entity.setCustomer(getCustomerById(shoppingCart));
        entity.setTotalAmount(shoppingCart.totalAmount().value());
        entity.setTotalItems(shoppingCart.totalItems().value());
        entity.setCreatedAt(shoppingCart.createdAt());

        mergeItems(entity, shoppingCart);

        entity.addEvents(shoppingCart.domainEvents());
    }

    private void mergeItems(
            ShoppingCartPersistenceEntity entity,
            ShoppingCart shoppingCart
    ) {
        Set<Long> domainItemIds = shoppingCart.items().stream()
                .map(item -> item.id().value().toLong())
                .collect(Collectors.toSet());

        entity.getItems().removeIf(
                persistenceItem -> !domainItemIds.contains(persistenceItem.getId())
        );

        shoppingCart.items().forEach(domainItem -> {
            entity.getItems().stream()
                    .filter(persistenceItem ->
                            persistenceItem.getId().equals(
                                    domainItem.id().value().toLong()
                            )
                    )
                    .findFirst()
                    .ifPresentOrElse(
                            persistenceItem ->
                                    mergeItem(persistenceItem, domainItem),
                            () -> entity.addItem(
                                    mergeItem(
                                            new ShoppingCartItemPersistenceEntity(),
                                            domainItem
                                    )
                            )
                    );
        });
    }

    private CustomerPersistenceEntity getCustomerById(ShoppingCart shoppingCart) {
        return customerPersistenceEntityRepository.getReferenceById(shoppingCart.customerId().value().toLong());
    }

    private Set<ShoppingCartItemPersistenceEntity> toItemsEntities(
            Set<ShoppingCartItem> source
    ) {
        return source.stream()
                .map(item -> this.mergeItem(
                        new ShoppingCartItemPersistenceEntity(),
                        item
                ))
                .collect(Collectors.toSet());
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
}
