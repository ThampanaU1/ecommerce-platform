package com.ecommerce.backend.catalog.service;

import com.ecommerce.backend.catalog.dto.AdjustStockRequest;
import com.ecommerce.backend.catalog.dto.InventoryResponse;
import com.ecommerce.backend.catalog.dto.SetInventoryRequest;
import com.ecommerce.backend.catalog.entity.Inventory;
import com.ecommerce.backend.catalog.entity.Product;
import com.ecommerce.backend.catalog.repository.InventoryRepository;
import com.ecommerce.backend.catalog.repository.ProductRepository;
import com.ecommerce.backend.common.exception.BadRequestException;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    @Transactional
    public InventoryResponse setInventory(Long productId, SetInventoryRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Inventory inventory = inventoryRepository.findByProduct(product)
                .orElseGet(() -> {
                    Inventory newInventory = new Inventory();
                    newInventory.setProduct(product);
                    newInventory.setReservedQuantity(0);
                    return newInventory;
                });

        inventory.setAvailableQuantity(request.getAvailableQuantity());
        inventory.setReorderLevel(request.getReorderLevel());
        inventory.setUpdatedAt(LocalDateTime.now());

        Inventory saved = inventoryRepository.save(inventory);
        return toResponse(saved);
    }

    @Transactional
    public InventoryResponse adjustStock(Long productId, AdjustStockRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Inventory inventory = inventoryRepository.findByProduct(product)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not set up for this product"));

        int newQuantity = inventory.getAvailableQuantity() + request.getQuantityChange();

        if (newQuantity < 0) {
            throw new BadRequestException("Insufficient stock: cannot reduce below 0");
        }

        inventory.setAvailableQuantity(newQuantity);
        inventory.setUpdatedAt(LocalDateTime.now());

        Inventory saved = inventoryRepository.save(inventory);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public InventoryResponse getInventory(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Inventory inventory = inventoryRepository.findByProduct(product)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not set up for this product"));

        return toResponse(inventory);
    }

    private InventoryResponse toResponse(Inventory inventory) {
        return new InventoryResponse(
                inventory.getId(),
                inventory.getProduct().getId(),
                inventory.getAvailableQuantity(),
                inventory.getReservedQuantity(),
                inventory.getReorderLevel(),
                inventory.getUpdatedAt()
        );
    }
}