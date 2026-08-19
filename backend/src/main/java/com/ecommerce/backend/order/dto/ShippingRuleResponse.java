package com.ecommerce.backend.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ShippingRuleResponse {
    private Long id;
    private String name;
    private String regionPattern;
    private BigDecimal minOrderFreeShipping;
    private BigDecimal baseCharge;
    private Boolean isActive;
}