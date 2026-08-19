package com.ecommerce.backend.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipping_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(name = "region_pattern", nullable = false, length = 100)
    private String regionPattern;

    @Column(name = "min_order_free_shipping", precision = 12, scale = 2)
    private BigDecimal minOrderFreeShipping;

    @Column(name = "base_charge", nullable = false, precision = 12, scale = 2)
    private BigDecimal baseCharge;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}