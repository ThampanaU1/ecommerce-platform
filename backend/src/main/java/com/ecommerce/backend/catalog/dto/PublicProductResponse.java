package com.ecommerce.backend.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class PublicProductResponse {

    private Long id;
    private String sku;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private Boolean featured;
    private BigDecimal mrp;
    private BigDecimal sellingPrice;
    private Integer availableQuantity;
    private String primaryImageUrl;
}