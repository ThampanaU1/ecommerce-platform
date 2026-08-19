package com.ecommerce.backend.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemResponse {

    private String productName;
    private String sku;
    private BigDecimal unitPrice;
    private Integer quantity;
    private BigDecimal lineTotal;
}