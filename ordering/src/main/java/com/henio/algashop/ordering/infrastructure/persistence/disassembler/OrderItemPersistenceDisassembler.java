package com.henio.algashop.ordering.infrastructure.persistence.disassembler;

import com.henio.algashop.ordering.domain.model.entity.OrderItem;
import com.henio.algashop.ordering.domain.model.valueobject.Money;
import com.henio.algashop.ordering.domain.model.valueobject.ProductName;
import com.henio.algashop.ordering.domain.model.valueobject.Quantity;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderId;
import com.henio.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import com.henio.algashop.ordering.domain.model.valueobject.id.ProductId;
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
