package com.ecommerce.backend.order.controller;

import com.ecommerce.backend.order.dto.AdminOrderResponse;
import com.ecommerce.backend.order.dto.UpdateOrderStatusRequest;
import com.ecommerce.backend.order.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final CheckoutService checkoutService;

    @GetMapping
    public ResponseEntity<List<AdminOrderResponse>> getAllOrders() {
        return ResponseEntity.ok(checkoutService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminOrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(checkoutService.getOrderById(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AdminOrderResponse> updateStatus(@PathVariable Long id,
                                                           @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(checkoutService.updateStatus(id, request.getStatus()));
    }
}