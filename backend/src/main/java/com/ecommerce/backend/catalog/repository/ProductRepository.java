package com.ecommerce.backend.catalog.repository;

import com.ecommerce.backend.catalog.entity.Category;
import com.ecommerce.backend.catalog.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySku(String sku);

    List<Product> findByCategory(Category category);
}