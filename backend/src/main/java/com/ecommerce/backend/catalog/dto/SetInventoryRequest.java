package com.ecommerce.backend.catalog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetInventoryRequest {

    @NotNull(message = "Available quantity is required")
    @Min(value = 0, message = "Available quantity cannot be negative")
    private Integer availableQuantity;

    @NotNull(message = "Reorder level is required")
    @Min(value = 0, message = "Reorder level cannot be negative")
    private Integer reorderLevel;
}