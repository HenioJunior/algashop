package com.henio.algashop.product.catalog.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@Data
@Entity
public class Category {

    @Id
    private UUID id = UUID.randomUUID();
    private String name;
    private Boolean enabled;
}
