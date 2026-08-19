package com.ecommerce.backend.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ApplyCouponRequest {

    @NotBlank(message = "Coupon code is required")
    private String code;

    private BigDecimal orderSubtotal;
}