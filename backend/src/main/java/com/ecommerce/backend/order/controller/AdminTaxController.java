package com.ecommerce.backend.order.controller;

import com.ecommerce.backend.order.dto.TaxConfigRequest;
import com.ecommerce.backend.order.dto.TaxConfigResponse;
import com.ecommerce.backend.order.service.ShippingTaxService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/tax-configs")
@RequiredArgsConstructor
public class AdminTaxController {

    private final ShippingTaxService shippingTaxService;

    @GetMapping
    public ResponseEntity<List<TaxConfigResponse>> getAll() {
        return ResponseEntity.ok(shippingTaxService.getAllTaxConfigs());
    }

    @PostMapping
    public ResponseEntity<TaxConfigResponse> create(@Valid @RequestBody TaxConfigRequest request) {
        return ResponseEntity.ok(shippingTaxService.createTaxConfig(request));
    }
}