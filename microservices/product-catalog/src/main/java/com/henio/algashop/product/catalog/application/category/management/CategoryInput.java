package com.henio.algashop.product.catalog.application.category.management;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class CategoryInput {
    @NotBlank
    private String name;
}
