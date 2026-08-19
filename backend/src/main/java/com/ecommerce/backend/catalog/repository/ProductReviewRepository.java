package com.ecommerce.backend.catalog.repository;

import com.ecommerce.backend.catalog.entity.Product;
import com.ecommerce.backend.catalog.entity.ProductReview;
import com.ecommerce.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductReviewRepository extends JpaRepository<ProductReview, Long> {

    List<ProductReview> findByProductAndStatus(Product product, String status);

    List<ProductReview> findAllByOrderByCreatedAtDesc();

    Optional<ProductReview> findByProductAndUser(Product product, User user);
}