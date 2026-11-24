package com.henio.algashop.product.catalog.application.category.management;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
public class CategoryInput {
    @NotBlank
    private String name;
    @NotNull
    private Boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }
}
