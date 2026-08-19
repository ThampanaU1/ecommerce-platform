package com.ecommerce.backend.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class AdminOrderResponse {

    private Long id;
    private String orderNumber;
    private String customerName;
    private String customerEmail;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal shippingTotal;
    private BigDecimal grandTotal;
    private LocalDateTime placedAt;
    private String shippingCity;
    private String shippingState;
    private List<OrderItemResponse> items;
}