package com.ecommerce.backend.catalog.controller;

import com.ecommerce.backend.catalog.dto.BannerResponse;
import com.ecommerce.backend.catalog.dto.PublicCategoryResponse;
import com.ecommerce.backend.catalog.dto.PublicProductResponse;
import com.ecommerce.backend.catalog.service.BannerService;
import com.ecommerce.backend.catalog.service.StorefrontService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StorefrontController {

    private final StorefrontService storefrontService;
    private final BannerService bannerService;

    @GetMapping("/banners")
    public ResponseEntity<List<BannerResponse>> getBanners() {
        return ResponseEntity.ok(bannerService.getActiveBanners());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<PublicCategoryResponse>> getCategories() {
        return ResponseEntity.ok(storefrontService.getActiveCategories());
    }

    @GetMapping("/products/{id}/images")
    public ResponseEntity<List<String>> getProductImages(@PathVariable Long id) {
        return ResponseEntity.ok(storefrontService.getProductImages(id));
    }

    @GetMapping("/products")
    public ResponseEntity<List<PublicProductResponse>> getProducts() {
        return ResponseEntity.ok(storefrontService.getActiveProducts());
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<PublicProductResponse> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(storefrontService.getProductById(id));
    }

    @GetMapping("/categories/{categoryId}/products")
    public ResponseEntity<List<PublicProductResponse>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(storefrontService.getProductsByCategory(categoryId));
    }
}