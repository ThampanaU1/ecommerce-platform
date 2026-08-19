package com.ecommerce.backend.catalog.controller;

import com.ecommerce.backend.catalog.dto.ReviewRequest;
import com.ecommerce.backend.catalog.dto.ReviewResponse;
import com.ecommerce.backend.catalog.service.ProductReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products/{productId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ProductReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getApprovedReviews(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getApprovedReviews(productId));
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> submitReview(Authentication authentication,
                                                       @PathVariable Long productId,
                                                       @Valid @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.submitReview(authentication.getName(), productId, request));
    }
}