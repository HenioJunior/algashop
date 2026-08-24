package com.henio.algashop.ordering.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_shopping_cart_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EntityListeners(AuditingEntityListener.class)
public class ShoppingCartItemPersistenceEntity {

    @Id
    @EqualsAndHashCode.Include
    private Long id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private ShoppingCartPersistenceEntity shoppingCart;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private boolean available;
    private BigDecimal totalAmount;

    @CreatedBy
    private UUID createdByUserId;
    @CreatedDate
    private OffsetDateTime createdAt;
    @LastModifiedDate
    private OffsetDateTime lastModifiedAt;
    @LastModifiedBy
    private UUID lastModifiedByUserId;

    @Version
    private Long version;

    private ShoppingCartPersistenceEntity getShoppingCart() {
        return shoppingCart;
    }

    public Long id() {
        return id;
    }

    public Long getShoppingCartId() {
        if (getShoppingCart() == null) {
            return null;
        }
        return getShoppingCart().getId();
    }
}
