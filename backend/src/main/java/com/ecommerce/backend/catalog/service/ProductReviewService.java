package com.ecommerce.backend.catalog.service;

import com.ecommerce.backend.catalog.dto.ReviewRequest;
import com.ecommerce.backend.catalog.dto.ReviewResponse;
import com.ecommerce.backend.catalog.entity.Product;
import com.ecommerce.backend.catalog.entity.ProductReview;
import com.ecommerce.backend.catalog.repository.ProductRepository;
import com.ecommerce.backend.catalog.repository.ProductReviewRepository;
import com.ecommerce.backend.common.exception.BadRequestException;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import com.ecommerce.backend.user.entity.User;
import com.ecommerce.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewService {

    private final ProductReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReviewResponse submitReview(String userEmail, Long productId, ReviewRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (reviewRepository.findByProductAndUser(product, user).isPresent()) {
            throw new BadRequestException("You have already reviewed this product");
        }

        ProductReview review = new ProductReview();
        review.setProduct(product);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setStatus("PENDING");
        review.setCreatedAt(LocalDateTime.now());

        ProductReview saved = reviewRepository.save(review);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getApprovedReviews(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return reviewRepository.findByProductAndStatus(product, "APPROVED").stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getAllForAdmin() {
        return reviewRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ReviewResponse moderate(Long id, String status) {
        ProductReview review = reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));

        review.setStatus(status);
        return toResponse(reviewRepository.save(review));
    }

    private ReviewResponse toResponse(ProductReview review) {
        return new ReviewResponse(
                review.getId(),
                review.getProduct().getId(),
                review.getProduct().getName(),
                review.getUser().getName(),
                review.getRating(),
                review.getComment(),
                review.getStatus(),
                review.getCreatedAt()
        );
    }
}