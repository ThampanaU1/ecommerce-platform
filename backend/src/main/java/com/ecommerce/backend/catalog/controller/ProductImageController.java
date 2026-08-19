package com.ecommerce.backend.catalog.controller;

import com.ecommerce.backend.catalog.dto.ProductImageRequest;
import com.ecommerce.backend.catalog.dto.ProductImageResponse;
import com.ecommerce.backend.catalog.service.ProductImageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/products/{productId}/images")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    @PostMapping
    public ResponseEntity<ProductImageResponse> addImage(@PathVariable Long productId,
                                                         @Valid @RequestBody ProductImageRequest request) {
        return ResponseEntity.ok(productImageService.addImage(productId, request));
    }

    @PatchMapping("/{imageId}/primary")
    public ResponseEntity<ProductImageResponse> setPrimary(@PathVariable Long productId,
                                                           @PathVariable Long imageId) {
        return ResponseEntity.ok(productImageService.setPrimary(productId, imageId));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long productId,
                                            @PathVariable Long imageId) {
        productImageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductImageResponse>> getImages(@PathVariable Long productId) {
        return ResponseEntity.ok(productImageService.getImages(productId));
    }
}