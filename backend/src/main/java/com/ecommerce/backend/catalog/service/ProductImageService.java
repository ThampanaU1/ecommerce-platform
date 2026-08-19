package com.ecommerce.backend.catalog.service;

import com.ecommerce.backend.catalog.dto.ProductImageRequest;
import com.ecommerce.backend.catalog.dto.ProductImageResponse;
import com.ecommerce.backend.catalog.entity.Product;
import com.ecommerce.backend.catalog.entity.ProductImage;
import com.ecommerce.backend.catalog.repository.ProductImageRepository;
import com.ecommerce.backend.catalog.repository.ProductRepository;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ProductImageResponse addImage(Long productId, ProductImageRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        boolean isPrimary = request.getIsPrimary() != null ? request.getIsPrimary() : false;

        // If this new image is being set as primary, unset any existing primary image first
        if (isPrimary) {
            clearExistingPrimary(product);
        }

        ProductImage image = new ProductImage();
        image.setProduct(product);
        image.setImageUrl(request.getImageUrl());
        image.setIsPrimary(isPrimary);
        image.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);

        ProductImage saved = productImageRepository.save(image);
        return toResponse(saved);
    }

    @Transactional
    public ProductImageResponse setPrimary(Long productId, Long imageId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        clearExistingPrimary(product);

        image.setIsPrimary(true);
        ProductImage saved = productImageRepository.save(image);
        return toResponse(saved);
    }

    @Transactional
    public void deleteImage(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        productImageRepository.delete(image);
    }

    @Transactional(readOnly = true)
    public List<ProductImageResponse> getImages(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return productImageRepository.findByProduct(product)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void clearExistingPrimary(Product product) {
        List<ProductImage> images = productImageRepository.findByProduct(product);
        images.stream()
                .filter(ProductImage::getIsPrimary)
                .forEach(img -> {
                    img.setIsPrimary(false);
                    productImageRepository.save(img);
                });
    }

    private ProductImageResponse toResponse(ProductImage image) {
        return new ProductImageResponse(
                image.getId(),
                image.getProduct().getId(),
                image.getImageUrl(),
                image.getIsPrimary(),
                image.getDisplayOrder()
        );
    }
}