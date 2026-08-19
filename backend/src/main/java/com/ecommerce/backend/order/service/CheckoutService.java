package com.ecommerce.backend.order.service;

import com.ecommerce.backend.catalog.entity.Inventory;
import com.ecommerce.backend.catalog.entity.Product;
import com.ecommerce.backend.catalog.entity.ProductPrice;
import com.ecommerce.backend.catalog.repository.InventoryRepository;
import com.ecommerce.backend.catalog.repository.ProductPriceRepository;
import com.ecommerce.backend.catalog.repository.ProductRepository;
import com.ecommerce.backend.common.exception.BadRequestException;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import com.ecommerce.backend.order.dto.*;
import com.ecommerce.backend.order.entity.Address;
import com.ecommerce.backend.order.entity.Order;
import com.ecommerce.backend.order.entity.OrderItem;
import com.ecommerce.backend.order.repository.AddressRepository;
import com.ecommerce.backend.order.repository.OrderItemRepository;
import com.ecommerce.backend.order.repository.OrderRepository;
import com.ecommerce.backend.user.entity.User;
import com.ecommerce.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.backend.order.entity.Coupon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class CheckoutService {
    private final ShippingTaxService shippingTaxService;
    private final CouponService couponService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final InventoryRepository inventoryRepository;


    @Transactional
    public OrderResponse placeOrder(String userEmail, CheckoutRequest request) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Address address = new Address();
        address.setUser(user);
        address.setLabel(request.getShippingAddress().getLabel());
        address.setLine1(request.getShippingAddress().getLine1());
        address.setLine2(request.getShippingAddress().getLine2());
        address.setCity(request.getShippingAddress().getCity());
        address.setState(request.getShippingAddress().getState());
        address.setPincode(request.getShippingAddress().getPincode());
        address.setCountry(request.getShippingAddress().getCountry());
        address.setIsDefault(false);
        address.setCreatedAt(LocalDateTime.now());
        Address savedAddress = addressRepository.save(address);

        BigDecimal subtotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CheckoutRequest.CheckoutItem item : request.getItems()) {

            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + item.getProductId()));

            if (!"ACTIVE".equals(product.getStatus())) {
                throw new BadRequestException("Product is not available: " + product.getName());
            }

            ProductPrice price = productPriceRepository.findByProductAndActiveTrue(product)
                    .orElseThrow(() -> new BadRequestException("No price set for product: " + product.getName()));

            Inventory inventory = inventoryRepository.findByProduct(product)
                    .orElseThrow(() -> new BadRequestException("No stock available for product: " + product.getName()));

            if (inventory.getAvailableQuantity() < item.getQuantity()) {
                throw new BadRequestException("Insufficient stock for product: " + product.getName());
            }

            BigDecimal unitPrice = price.getSellingPrice();
            BigDecimal lineTotal = unitPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
            subtotal = subtotal.add(lineTotal);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setProductNameSnapshot(product.getName());
            orderItem.setSkuSnapshot(product.getSku());
            orderItem.setUnitPriceSnapshot(unitPrice);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setLineTotal(lineTotal);
            orderItems.add(orderItem);

            inventory.setAvailableQuantity(inventory.getAvailableQuantity() - item.getQuantity());
            inventory.setUpdatedAt(LocalDateTime.now());
            inventoryRepository.save(inventory);
        }

        BigDecimal discountTotal = BigDecimal.ZERO;
        Coupon appliedCoupon = null;

        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            discountTotal = couponService.validateAndCalculateDiscount(request.getCouponCode(), user, subtotal);
            appliedCoupon = couponService.getCouponEntityByCode(request.getCouponCode());
        }

        var activeTaxConfig = shippingTaxService.getActiveTaxConfig();
        var activeShippingRule = shippingTaxService.getActiveShippingRule();

        BigDecimal discountedSubtotal = subtotal.subtract(discountTotal);

        BigDecimal taxRate = activeTaxConfig.getTaxPercent().divide(new BigDecimal("100"));
        BigDecimal taxTotal = discountedSubtotal.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);

        BigDecimal freeShippingThreshold = activeShippingRule.getMinOrderFreeShipping();
        BigDecimal shippingTotal = (freeShippingThreshold != null && discountedSubtotal.compareTo(freeShippingThreshold) >= 0)
                ? BigDecimal.ZERO
                : activeShippingRule.getBaseCharge();

        BigDecimal grandTotal = discountedSubtotal.add(taxTotal).add(shippingTotal);

        Order order = new Order();
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setUser(user);
        order.setStatus("CONFIRMED");
        order.setSubtotal(subtotal);
        order.setDiscountTotal(discountTotal);
        order.setTaxTotal(taxTotal);
        order.setShippingTotal(shippingTotal);
        order.setGrandTotal(grandTotal);
        order.setShippingAddress(savedAddress);
        order.setCoupon(appliedCoupon);
        order.setPlacedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        if (appliedCoupon != null) {
            couponService.recordRedemption(appliedCoupon, user, savedOrder);
        }
        for (OrderItem item : orderItems) {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }

        return toResponse(savedOrder, orderItems);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return orderRepository.findByUserOrderByPlacedAtDesc(user).stream()
                .map(order -> toResponse(order, orderItemRepository.findByOrder(order)))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AdminOrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(order -> toAdminResponse(order, orderItemRepository.findByOrder(order)))
                .toList();
    }

    @Transactional(readOnly = true)
    public AdminOrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return toAdminResponse(order, orderItemRepository.findByOrder(order));
    }

    @Transactional
    public AdminOrderResponse updateStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        Order updated = orderRepository.save(order);

        return toAdminResponse(updated, orderItemRepository.findByOrder(updated));
    }

    private OrderResponse toResponse(Order order, List<OrderItem> items) {
        List<OrderItemResponse> itemResponses = items.stream()
                .map(item -> new OrderItemResponse(
                        item.getProductNameSnapshot(),
                        item.getSkuSnapshot(),
                        item.getUnitPriceSnapshot(),
                        item.getQuantity(),
                        item.getLineTotal()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus(),
                order.getSubtotal(),
                order.getDiscountTotal(),
                order.getTaxTotal(),
                order.getShippingTotal(),
                order.getGrandTotal(),
                order.getPlacedAt(),
                itemResponses
        );
    }

    private AdminOrderResponse toAdminResponse(Order order, List<OrderItem> items) {
        List<OrderItemResponse> itemResponses = items.stream()
                .map(item -> new OrderItemResponse(
                        item.getProductNameSnapshot(),
                        item.getSkuSnapshot(),
                        item.getUnitPriceSnapshot(),
                        item.getQuantity(),
                        item.getLineTotal()
                ))
                .toList();

        return new AdminOrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getUser().getName(),
                order.getUser().getEmail(),
                order.getStatus(),
                order.getSubtotal(),
                order.getTaxTotal(),
                order.getShippingTotal(),
                order.getGrandTotal(),
                order.getPlacedAt(),
                order.getShippingAddress().getCity(),
                order.getShippingAddress().getState(),
                itemResponses
        );
    }
}