package com.ecommerce.backend.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TaxConfigRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Tax percent is required")
    private BigDecimal taxPercent;
}