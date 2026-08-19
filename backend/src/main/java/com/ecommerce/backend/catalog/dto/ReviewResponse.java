package com.ecommerce.backend.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ReviewResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String customerName;
    private Integer rating;
    private String comment;
    private String status;
    private LocalDateTime createdAt;
}