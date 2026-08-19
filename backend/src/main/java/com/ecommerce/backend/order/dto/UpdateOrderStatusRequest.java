package com.ecommerce.backend.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {

    @NotBlank(message = "Status is required")
    private String status;
}