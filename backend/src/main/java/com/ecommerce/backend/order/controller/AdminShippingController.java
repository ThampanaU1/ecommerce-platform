package com.ecommerce.backend.order.controller;

import com.ecommerce.backend.order.dto.ShippingRuleRequest;
import com.ecommerce.backend.order.dto.ShippingRuleResponse;
import com.ecommerce.backend.order.service.ShippingTaxService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/shipping-rules")
@RequiredArgsConstructor
public class AdminShippingController {

    private final ShippingTaxService shippingTaxService;

    @GetMapping
    public ResponseEntity<List<ShippingRuleResponse>> getAll() {
        return ResponseEntity.ok(shippingTaxService.getAllShippingRules());
    }

    @PostMapping
    public ResponseEntity<ShippingRuleResponse> create(@Valid @RequestBody ShippingRuleRequest request) {
        return ResponseEntity.ok(shippingTaxService.createShippingRule(request));
    }
}