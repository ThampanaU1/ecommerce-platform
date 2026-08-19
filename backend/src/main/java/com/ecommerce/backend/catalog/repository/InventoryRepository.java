package com.ecommerce.backend.catalog.repository;

import com.ecommerce.backend.catalog.entity.Inventory;
import com.ecommerce.backend.catalog.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProduct(Product product);
}