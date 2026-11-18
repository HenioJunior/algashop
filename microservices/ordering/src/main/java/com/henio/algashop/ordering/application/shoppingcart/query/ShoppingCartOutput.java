package com.henio.algashop.ordering.application.shoppingcart.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShoppingCartOutput {
    private String id;
    private UUID customerId;
    private Integer totalItems;
    private BigDecimal totalAmount;
    private List<ShoppingCartItemOutput> items = new ArrayList<>();
}
