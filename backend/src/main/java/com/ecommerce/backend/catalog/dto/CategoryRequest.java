package com.ecommerce.backend.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

    @NotBlank(message = "Category name is required")
    @Size(max = 150, message = "Category name must not exceed 150 characters")
    private String name;

    private String description;

    private String imageUrl;

    @NotNull(message = "Display order is required")
    private Integer displayOrder;
}