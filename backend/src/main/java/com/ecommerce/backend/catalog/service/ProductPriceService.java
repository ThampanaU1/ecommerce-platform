package com.ecommerce.backend.catalog.service;

import com.ecommerce.backend.catalog.dto.ProductPriceResponse;
import com.ecommerce.backend.catalog.dto.SetPriceRequest;
import com.ecommerce.backend.catalog.entity.Product;
import com.ecommerce.backend.catalog.entity.ProductPrice;
import com.ecommerce.backend.catalog.repository.ProductPriceRepository;
import com.ecommerce.backend.catalog.repository.ProductRepository;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductPriceService {

    private final ProductPriceRepository productPriceRepository;
    private final ProductRepository productRepository;

    @Transactional
    public ProductPriceResponse setPrice(Long productId, SetPriceRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        LocalDateTime now = LocalDateTime.now();

        // Close out the currently active price, if one exists
        Optional<ProductPrice> currentPrice = productPriceRepository.findByProductAndActiveTrue(product);
        currentPrice.ifPresent(price -> {
            price.setActive(false);
            price.setValidTo(now);
            productPriceRepository.save(price);
        });

        // Create the new active price
        ProductPrice newPrice = new ProductPrice();
        newPrice.setProduct(product);
        newPrice.setMrp(request.getMrp());
        newPrice.setSellingPrice(request.getSellingPrice());
        newPrice.setActive(true);
        newPrice.setValidFrom(now);
        newPrice.setValidTo(null);
        newPrice.setCreatedAt(now);

        ProductPrice saved = productPriceRepository.save(newPrice);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public ProductPriceResponse getCurrentPrice(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ProductPrice currentPrice = productPriceRepository.findByProductAndActiveTrue(product)
                .orElseThrow(() -> new ResourceNotFoundException("No active price set for this product"));

        return toResponse(currentPrice);
    }

    @Transactional(readOnly = true)
    public List<ProductPriceResponse> getPriceHistory(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        return productPriceRepository.findByProductOrderByCreatedAtDesc(product)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ProductPriceResponse toResponse(ProductPrice price) {
        return new ProductPriceResponse(
                price.getId(),
                price.getProduct().getId(),
                price.getMrp(),
                price.getSellingPrice(),
                price.getActive(),
                price.getValidFrom(),
                price.getValidTo(),
                price.getCreatedAt()
        );
    }
}