package com.ecommerce.backend.catalog.repository;

import com.ecommerce.backend.catalog.entity.Product;
import com.ecommerce.backend.catalog.entity.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {

    Optional<ProductPrice> findByProductAndActiveTrue(Product product);

    List<ProductPrice> findByProductOrderByCreatedAtDesc(Product product);
}