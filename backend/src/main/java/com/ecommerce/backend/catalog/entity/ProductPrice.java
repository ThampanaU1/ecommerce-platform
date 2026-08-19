package com.ecommerce.backend.catalog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_prices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal mrp;

    @Column(name = "selling_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal sellingPrice;

    @Column(nullable = false)
    private Boolean active;

    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}