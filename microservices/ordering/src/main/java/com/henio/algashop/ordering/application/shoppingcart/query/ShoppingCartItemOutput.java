package com.henio.algashop.ordering.application.shoppingcart.query;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShoppingCartItemOutput {
    private String id;
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal totalAmount;
    private Boolean available;
}
