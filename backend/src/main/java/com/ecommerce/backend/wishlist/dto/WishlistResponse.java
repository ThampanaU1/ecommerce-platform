package com.ecommerce.backend.wishlist.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class WishlistResponse {

    private Long id;
    private Long productId;
    private String productName;
    private String productSku;
    private BigDecimal sellingPrice;
    private String primaryImageUrl;
    private Boolean inStock;
    private LocalDateTime addedAt;
}