package com.henio.algashop.ordering.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_order_item")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
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
