package com.ecommerce.backend.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ProductPriceResponse {

    private Long id;
    private Long productId;
    private BigDecimal mrp;
    private BigDecimal sellingPrice;
    private Boolean active;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;
    private LocalDateTime createdAt;
}