package com.ecommerce.backend.catalog.service;

import com.ecommerce.backend.catalog.dto.ProductRequest;
import com.ecommerce.backend.catalog.dto.ProductResponse;
import com.ecommerce.backend.catalog.entity.Category;
import com.ecommerce.backend.catalog.entity.Product;
import com.ecommerce.backend.catalog.repository.CategoryRepository;
import com.ecommerce.backend.catalog.repository.ProductRepository;
import com.ecommerce.backend.common.exception.BadRequestException;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductResponse create(ProductRequest request) {

        if (productRepository.findBySku(request.getSku()).isPresent()) {
            throw new BadRequestException("SKU already exists");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = new Product();
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(category);
        product.setStatus("ACTIVE");
        product.setFeatured(request.getFeatured() != null ? request.getFeatured() : false);
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());

        Product saved = productRepository.save(product);
        return toResponse(saved);
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setCategory(category);
        product.setFeatured(request.getFeatured() != null ? request.getFeatured() : product.getFeatured());
        product.setUpdatedAt(LocalDateTime.now());

        Product updated = productRepository.save(product);
        return toResponse(updated);
    }

    @Transactional
    public ProductResponse setStatus(Long id, String status) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        product.setStatus(status);
        product.setUpdatedAt(LocalDateTime.now());

        Product updated = productRepository.save(product);
        return toResponse(updated);
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return toResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getAll() {
        return productRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getSku(),
                product.getName(),
                product.getDescription(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getStatus(),
                product.getFeatured(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}