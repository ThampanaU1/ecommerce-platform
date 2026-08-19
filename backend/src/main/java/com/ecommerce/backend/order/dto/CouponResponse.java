package com.ecommerce.backend.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CouponResponse {

    private Long id;
    private String code;
    private String type;
    private BigDecimal value;
    private BigDecimal minOrderValue;
    private BigDecimal maxDiscount;
    private Integer usageLimitTotal;
    private Integer usageLimitPerUser;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive;
}