package com.ecommerce.backend.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PublicCategoryResponse {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer displayOrder;
}