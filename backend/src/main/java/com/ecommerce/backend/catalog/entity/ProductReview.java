package com.ecommerce.backend.catalog.entity;

import com.ecommerce.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, columnDefinition = "TINYINT")
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}