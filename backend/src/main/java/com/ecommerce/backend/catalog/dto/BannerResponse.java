package com.ecommerce.backend.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BannerResponse {
    private Long id;
    private String title;
    private String imageUrl;
    private String linkUrl;
    private Integer displayOrder;
    private Boolean isActive;
}