package com.ecommerce.backend.catalog.repository;

import com.ecommerce.backend.catalog.entity.Product;
import com.ecommerce.backend.catalog.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    List<ProductImage> findByProduct(Product product);
}