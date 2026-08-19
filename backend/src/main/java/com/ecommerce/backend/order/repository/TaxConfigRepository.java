package com.ecommerce.backend.order.repository;

import com.ecommerce.backend.order.entity.TaxConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaxConfigRepository extends JpaRepository<TaxConfig, Long> {

    Optional<TaxConfig> findByIsActiveTrue();
}