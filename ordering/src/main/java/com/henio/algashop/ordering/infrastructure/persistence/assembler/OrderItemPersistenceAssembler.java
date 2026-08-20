package com.henio.algashop.ordering.infrastructure.persistence.assembler;

import com.henio.algashop.ordering.domain.model.entity.Order;
import com.henio.algashop.ordering.domain.model.entity.OrderItem;
import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderItemPersistenceAssembler {

    public Set<OrderItemPersistenceEntity> mergeItems(Order order, OrderPersistenceEntity entity) {
        Set<OrderItem> newOrUpdatedItems = order.items();
        if(newOrUpdatedItems == null || newOrUpdatedItems.isEmpty()) {
            return new HashSet<>();
        }
        Set<OrderItemPersistenceEntity> existingItems = entity.getItems();

        if(existingItems == null || existingItems.isEmpty()) {
            return toItemsEntity(newOrUpdatedItems);
        }
        Map<Long, OrderItemPersistenceEntity> existingItemMap = existingItems.stream()
                .collect(Collectors.toMap(OrderItemPersistenceEntity::getId, item -> item));

        return newOrUpdatedItems.stream()
                .map(orderItem -> {
                    OrderItemPersistenceEntity itemPersistence = existingItemMap.getOrDefault(
                            orderItem.id().value().toLong(), new OrderItemPersistenceEntity()
                    );
                    return merge(itemPersistence, orderItem);
                })
                .collect(Collectors.toSet());
    }

    public OrderItemPersistenceEntity fromDomain(OrderItem orderItem) {
        return merge(new OrderItemPersistenceEntity(), orderItem);
    }

    public OrderItemPersistenceEntity merge(OrderItemPersistenceEntity orderItemPersistenceEntity,
                                             OrderItem orderItem) {
        return OrderItemPersistenceEntity.builder()
                .id(orderItem.id().value().toLong())
                .productId(orderItem.product().id().value().toLong())
                .productName(orderItem.product().name().value())
                .price(orderItem.product().price().value())
                .quantity(orderItem.quantity().value())
                .totalAmount(orderItem.totalAmount().value())
                .build();
    }

    public Set<OrderItemPersistenceEntity> toItemsEntity(Set<OrderItem> orderItems) {
        return orderItems
                .stream()
                .map(this::toItemEntity)
                .collect(Collectors.toSet());
    }

    private OrderItemPersistenceEntity toItemEntity(OrderItem item) {
        return OrderItemPersistenceEntity.builder()
                .id(item.id().value().toLong())
                .productId(item.id().value().toLong())
                .productName(item.product().name().toString())
                .price(new BigDecimal(item.product().price().toString()))
                .quantity(item.quantity().value())
                .totalAmount(new BigDecimal(item.totalAmount().toString()))
                .build();
    }
}
