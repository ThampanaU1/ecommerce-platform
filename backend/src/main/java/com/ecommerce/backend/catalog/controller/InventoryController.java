package com.ecommerce.backend.catalog.controller;

import com.ecommerce.backend.catalog.dto.AdjustStockRequest;
import com.ecommerce.backend.catalog.dto.InventoryResponse;
import com.ecommerce.backend.catalog.dto.SetInventoryRequest;
import com.ecommerce.backend.catalog.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/products/{productId}/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponse> setInventory(@PathVariable Long productId,
                                                          @Valid @RequestBody SetInventoryRequest request) {
        return ResponseEntity.ok(inventoryService.setInventory(productId, request));
    }

    @PatchMapping("/adjust")
    public ResponseEntity<InventoryResponse> adjustStock(@PathVariable Long productId,
                                                         @Valid @RequestBody AdjustStockRequest request) {
        return ResponseEntity.ok(inventoryService.adjustStock(productId, request));
    }

    @GetMapping
    public ResponseEntity<InventoryResponse> getInventory(@PathVariable Long productId) {
        return ResponseEntity.ok(inventoryService.getInventory(productId));
    }
}