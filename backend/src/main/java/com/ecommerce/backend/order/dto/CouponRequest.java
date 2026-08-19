package com.ecommerce.backend.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CouponRequest {

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Type is required")
    private String type;

    @NotNull(message = "Value is required")
    private BigDecimal value;

    private BigDecimal minOrderValue;

    private BigDecimal maxDiscount;

    private Integer usageLimitTotal;

    private Integer usageLimitPerUser;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;
}