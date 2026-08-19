package com.ecommerce.backend.order.service;

import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import com.ecommerce.backend.order.dto.*;
import com.ecommerce.backend.order.entity.ShippingRule;
import com.ecommerce.backend.order.entity.TaxConfig;
import com.ecommerce.backend.order.repository.ShippingRuleRepository;
import com.ecommerce.backend.order.repository.TaxConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingTaxService {

    private final ShippingRuleRepository shippingRuleRepository;
    private final TaxConfigRepository taxConfigRepository;

    @Transactional(readOnly = true)
    public List<ShippingRuleResponse> getAllShippingRules() {
        return shippingRuleRepository.findAll().stream()
                .map(this::toShippingResponse)
                .toList();
    }

    @Transactional
    public ShippingRuleResponse createShippingRule(ShippingRuleRequest request) {
        shippingRuleRepository.findByIsActiveTrue().ifPresent(existing -> {
            existing.setIsActive(false);
            shippingRuleRepository.save(existing);
        });

        ShippingRule rule = new ShippingRule();
        rule.setName(request.getName());
        rule.setRegionPattern(request.getRegionPattern() != null ? request.getRegionPattern() : "*");
        rule.setMinOrderFreeShipping(request.getMinOrderFreeShipping());
        rule.setBaseCharge(request.getBaseCharge());
        rule.setIsActive(true);
        rule.setCreatedAt(LocalDateTime.now());

        ShippingRule saved = shippingRuleRepository.save(rule);
        return toShippingResponse(saved);
    }

    @Transactional(readOnly = true)
    public ShippingRule getActiveShippingRule() {
        return shippingRuleRepository.findByIsActiveTrue()
                .orElseThrow(() -> new ResourceNotFoundException("No active shipping rule configured"));
    }

    @Transactional(readOnly = true)
    public List<TaxConfigResponse> getAllTaxConfigs() {
        return taxConfigRepository.findAll().stream()
                .map(this::toTaxResponse)
                .toList();
    }

    @Transactional
    public TaxConfigResponse createTaxConfig(TaxConfigRequest request) {
        taxConfigRepository.findByIsActiveTrue().ifPresent(existing -> {
            existing.setIsActive(false);
            taxConfigRepository.save(existing);
        });

        TaxConfig config = new TaxConfig();
        config.setName(request.getName());
        config.setTaxPercent(request.getTaxPercent());
        config.setIsActive(true);
        config.setCreatedAt(LocalDateTime.now());

        TaxConfig saved = taxConfigRepository.save(config);
        return toTaxResponse(saved);
    }

    @Transactional(readOnly = true)
    public TaxConfig getActiveTaxConfig() {
        return taxConfigRepository.findByIsActiveTrue()
                .orElseThrow(() -> new ResourceNotFoundException("No active tax configuration"));
    }

    private ShippingRuleResponse toShippingResponse(ShippingRule rule) {
        return new ShippingRuleResponse(
                rule.getId(), rule.getName(), rule.getRegionPattern(),
                rule.getMinOrderFreeShipping(), rule.getBaseCharge(), rule.getIsActive()
        );
    }

    private TaxConfigResponse toTaxResponse(TaxConfig config) {
        return new TaxConfigResponse(
                config.getId(), config.getName(), config.getTaxPercent(), config.getIsActive()
        );
    }
}