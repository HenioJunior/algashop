package com.henio.algashop.product.catalog.presentaion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CategoryMinimalOutput {
    private UUID id;
    private String name;
}
