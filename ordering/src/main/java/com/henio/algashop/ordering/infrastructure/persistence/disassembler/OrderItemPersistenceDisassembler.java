package com.henio.algashop.ordering.infrastructure.persistence.disassembler;

import com.henio.algashop.ordering.domain.model.order.OrderItem;
import com.henio.algashop.ordering.domain.model.commons.Money;
import com.henio.algashop.ordering.domain.model.product.ProductName;
import com.henio.algashop.ordering.domain.model.commons.Quantity;
import com.henio.algashop.ordering.domain.model.order.OrderId;
import com.henio.algashop.ordering.domain.model.order.OrderItemId;
import com.henio.algashop.ordering.domain.model.product.ProductId;
import com.henio.algashop.ordering.infrastructure.persistence.entity.OrderItemPersistenceEntity;
import io.hypersistence.tsid.TSID;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderItemPersistenceDisassembler {

    public OrderItem toDomain(OrderItemPersistenceEntity persistenceEntity) {
        return OrderItem.existing()
                .id(new OrderItemId(TSID.from(persistenceEntity.getId())))
                .orderId(new OrderId(TSID.from(persistenceEntity.getOrderId())))
                .productId(new ProductId(TSID.from(persistenceEntity.getProductId())))
                .productName(new ProductName(persistenceEntity.getProductName()))
                .price(new Money(persistenceEntity.getPrice()))
                .quantity(new Quantity(persistenceEntity.getQuantity()))
                .totalAmount(new Money(persistenceEntity.getTotalAmount()))
                .build();
    }

    public Set<OrderItem> toDomain(Set<OrderItemPersistenceEntity> items) {
        return items.stream().map(this::toDomain).collect(Collectors.toSet());
    }
}
