package com.ecommerce.backend.order.controller;

import com.ecommerce.backend.order.dto.CheckoutRequest;
import com.ecommerce.backend.order.dto.OrderResponse;
import com.ecommerce.backend.order.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(Authentication authentication,
                                                    @Valid @RequestBody CheckoutRequest request) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(checkoutService.placeOrder(userEmail, request));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(checkoutService.getMyOrders(authentication.getName()));
    }
}