package com.henio.algashop.ordering.infrastructure.persistence.shoppingcart;

import com.henio.algashop.ordering.infrastructure.persistence.customer.CustomerPersistenceEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Table(name = "tb_shopping_cart")
@Data
@NoArgsConstructor
@ToString(of = "id")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@EntityListeners(AuditingEntityListener.class)
public class ShoppingCartPersistenceEntity
        extends AbstractAggregateRoot<ShoppingCartPersistenceEntity> {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @JoinColumn(name = "customer_id", nullable = false)
    @ManyToOne(optional = false)
    private CustomerPersistenceEntity customer;
    private BigDecimal totalAmount;
    private Integer totalItems;
    @OneToMany(
            mappedBy = "shoppingCart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<ShoppingCartItemPersistenceEntity> items;

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

    @Builder(toBuilder = true)
    public ShoppingCartPersistenceEntity(
            Long id,
            CustomerPersistenceEntity customer,
            BigDecimal totalAmount,
            Integer totalItems,
            OffsetDateTime createdAt,
            Set<ShoppingCartItemPersistenceEntity> items,
            Long version
    ) {
        this.id = id;
        this.customer = customer;
        this.totalAmount = totalAmount;
        this.totalItems = totalItems;
        this.createdAt = createdAt;
        this.addItem(items);
        this.version = version;
    }

    public void addItem(Set<ShoppingCartItemPersistenceEntity> items) {
        if (items == null) {
            return;
        }

        if (this.items == null) {
            this.items = new HashSet<>();
        }

        items.forEach(this::addItem);
    }

    public void addItem(ShoppingCartItemPersistenceEntity item) {
        if (item == null) {
            return;
        }

        item.setShoppingCart(this);
        this.items.add(item);
    }

    public Long getCustomerId() {
        if (customer == null) {
            return null;
        }
        return customer.getId();
    }

    public void replaceItems(Set<ShoppingCartItemPersistenceEntity> items) {
        this.items.clear();

        if (items != null) {
            items.forEach(this::addItem);
        }
    }

    public Collection<Object> getEvents() {
        return super.domainEvents();
    }

    public void addEvents(Collection<Object> events) {
        Objects.requireNonNull(events, "Events must not be null");
        events.forEach(this::registerEvent);
    }
}
