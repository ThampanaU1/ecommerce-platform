package com.ecommerce.backend.catalog.controller;

import com.ecommerce.backend.catalog.dto.ProductPriceResponse;
import com.ecommerce.backend.catalog.dto.SetPriceRequest;
import com.ecommerce.backend.catalog.service.ProductPriceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/products/{productId}/price")
@RequiredArgsConstructor
public class ProductPriceController {

    private final ProductPriceService productPriceService;

    @PostMapping
    public ResponseEntity<ProductPriceResponse> setPrice(@PathVariable Long productId,
                                                         @Valid @RequestBody SetPriceRequest request) {
        return ResponseEntity.ok(productPriceService.setPrice(productId, request));
    }

    @GetMapping("/current")
    public ResponseEntity<ProductPriceResponse> getCurrentPrice(@PathVariable Long productId) {
        return ResponseEntity.ok(productPriceService.getCurrentPrice(productId));
    }

    @GetMapping("/history")
    public ResponseEntity<List<ProductPriceResponse>> getPriceHistory(@PathVariable Long productId) {
        return ResponseEntity.ok(productPriceService.getPriceHistory(productId));
    }
}
