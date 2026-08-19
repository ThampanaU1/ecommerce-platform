package com.ecommerce.backend.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String sku;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private String status;
    private Boolean featured;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}