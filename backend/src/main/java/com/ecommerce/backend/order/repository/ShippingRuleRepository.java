package com.ecommerce.backend.order.repository;

import com.ecommerce.backend.order.entity.ShippingRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShippingRuleRepository extends JpaRepository<ShippingRule, Long> {

    Optional<ShippingRule> findByIsActiveTrue();
}