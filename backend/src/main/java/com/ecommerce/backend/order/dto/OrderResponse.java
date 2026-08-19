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
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal discountTotal;
    private BigDecimal taxTotal;
    private BigDecimal shippingTotal;
    private BigDecimal grandTotal;
    private LocalDateTime placedAt;
    private List<OrderItemResponse> items;
}