package com.ecommerce.backend.catalog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "banners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 200)
    private String title;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "link_url", length = 500)
    private String linkUrl;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}