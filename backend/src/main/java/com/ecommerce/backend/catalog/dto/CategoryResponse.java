package com.ecommerce.backend.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CategoryResponse {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer displayOrder;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}