package com.ecommerce.backend.catalog.controller;

import com.ecommerce.backend.catalog.dto.ReviewResponse;
import com.ecommerce.backend.catalog.service.ProductReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ProductReviewService reviewService;

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAll() {
        return ResponseEntity.ok(reviewService.getAllForAdmin());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ReviewResponse> moderate(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(reviewService.moderate(id, body.get("status")));
    }
}