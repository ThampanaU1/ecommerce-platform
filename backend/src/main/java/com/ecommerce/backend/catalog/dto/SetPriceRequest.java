package com.ecommerce.backend.catalog.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SetPriceRequest {

    @NotNull(message = "MRP is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "MRP must be greater than 0")
    private BigDecimal mrp;

    @NotNull(message = "Selling price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Selling price must be greater than 0")
    private BigDecimal sellingPrice;
}