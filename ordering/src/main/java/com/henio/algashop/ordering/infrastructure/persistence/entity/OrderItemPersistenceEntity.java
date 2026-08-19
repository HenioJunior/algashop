package com.henio.algashop.ordering.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrderItemPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalAmount;

    @Setter
    @JoinColumn
    @ManyToOne(optional = false)
    private OrderPersistenceEntity order;

    public Long getOrderId() {
        if (order == null) {
            return null;
        }
        return order.getId();
    }
}
