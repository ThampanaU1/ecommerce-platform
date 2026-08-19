package com.ecommerce.backend.catalog.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdjustStockRequest {

    @NotNull(message = "Quantity change is required")
    private Integer quantityChange;
}