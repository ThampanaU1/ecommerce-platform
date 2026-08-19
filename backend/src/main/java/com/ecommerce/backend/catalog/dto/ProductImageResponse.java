package com.ecommerce.backend.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductImageResponse {

    private Long id;
    private Long productId;
    private String imageUrl;
    private Boolean isPrimary;
    private Integer displayOrder;
}