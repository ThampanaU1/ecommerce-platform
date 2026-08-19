package com.ecommerce.backend.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BannerRequest {

    private String title;

    @NotBlank(message = "Image URL is required")
    private String imageUrl;

    private String linkUrl;

    private Integer displayOrder;
}