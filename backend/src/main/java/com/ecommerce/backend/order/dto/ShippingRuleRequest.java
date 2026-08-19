package com.ecommerce.backend.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ShippingRuleRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String regionPattern;

    private BigDecimal minOrderFreeShipping;

    @NotNull(message = "Base charge is required")
    private BigDecimal baseCharge;
}