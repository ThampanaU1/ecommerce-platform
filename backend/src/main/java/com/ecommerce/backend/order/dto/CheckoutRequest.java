package com.ecommerce.backend.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CheckoutRequest {

    @NotNull(message = "Shipping address is required")
    @Valid
    private AddressRequest shippingAddress;
    private String couponCode;

    @NotEmpty(message = "Cart cannot be empty")
    @Valid
    private List<CheckoutItem> items;

    @Getter
    @Setter
    public static class CheckoutItem {

        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Quantity is required")
        private Integer quantity;
    }
}