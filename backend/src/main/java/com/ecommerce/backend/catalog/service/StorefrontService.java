package com.ecommerce.backend.catalog.service;

import com.ecommerce.backend.catalog.dto.PublicCategoryResponse;
import com.ecommerce.backend.catalog.dto.PublicProductResponse;
import com.ecommerce.backend.catalog.entity.Category;
import com.ecommerce.backend.catalog.entity.Product;
import com.ecommerce.backend.catalog.entity.ProductImage;
import com.ecommerce.backend.catalog.entity.ProductPrice;
import com.ecommerce.backend.catalog.repository.*;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorefrontService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional(readOnly = true)
    public List<PublicCategoryResponse> getActiveCategories() {
        return categoryRepository.findAll().stream()
                .filter(Category::getActive)
                .map(this::toCategoryResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PublicProductResponse> getActiveProducts() {
        return productRepository.findAll().stream()
                .filter(p -> "ACTIVE".equals(p.getStatus()))
                .map(this::toProductResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PublicProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .filter(p -> "ACTIVE".equals(p.getStatus()))
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return toProductResponse(product);
    }

    @Transactional(readOnly = true)
    public List<String> getProductImages(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return productImageRepository.findByProduct(product).stream()
                .map(ProductImage::getImageUrl)
                .toList();
    }


    @Transactional(readOnly = true)
    public List<PublicProductResponse> getProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        return productRepository.findByCategory(category).stream()
                .filter(p -> "ACTIVE".equals(p.getStatus()))
                .map(this::toProductResponse)
                .toList();
    }

    private PublicCategoryResponse toCategoryResponse(Category category) {
        return new PublicCategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getImageUrl(),
                category.getDisplayOrder()
        );
    }

    private PublicProductResponse toProductResponse(Product product) {

        Optional<ProductPrice> priceOpt = productPriceRepository.findByProductAndActiveTrue(product);
        BigDecimal mrp = priceOpt.map(ProductPrice::getMrp).orElse(null);
        BigDecimal sellingPrice = priceOpt.map(ProductPrice::getSellingPrice).orElse(null);

        Integer availableQuantity = inventoryRepository.findByProduct(product)
                .map(inv -> inv.getAvailableQuantity())
                .orElse(0);

        String primaryImageUrl = productImageRepository.findByProduct(product).stream()
                .filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))
                .findFirst()
                .map(ProductImage::getImageUrl)
                .orElse(null);

        return new PublicProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getFeatured(),
                mrp,
                sellingPrice,
                availableQuantity,
                primaryImageUrl
        );
    }
}