package com.ecommerce.backend.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class InventoryResponse {

    private Long id;
    private Long productId;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private Integer reorderLevel;
    private LocalDateTime updatedAt;
}