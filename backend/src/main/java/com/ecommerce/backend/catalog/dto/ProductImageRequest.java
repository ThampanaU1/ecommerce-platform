package com.ecommerce.backend.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImageRequest {

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    private Boolean isPrimary;

    private Integer displayOrder;
}