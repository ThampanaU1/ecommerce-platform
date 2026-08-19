package com.ecommerce.backend.order.service;

import com.ecommerce.backend.common.exception.BadRequestException;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import com.ecommerce.backend.order.dto.CouponRequest;
import com.ecommerce.backend.order.dto.CouponResponse;
import com.ecommerce.backend.order.entity.Coupon;
import com.ecommerce.backend.order.entity.CouponRedemption;
import com.ecommerce.backend.order.entity.Order;
import com.ecommerce.backend.order.repository.CouponRedemptionRepository;
import com.ecommerce.backend.order.repository.CouponRepository;
import com.ecommerce.backend.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final CouponRedemptionRepository couponRedemptionRepository;

    @Transactional
    public CouponResponse create(CouponRequest request) {

        if (couponRepository.findByCode(request.getCode()).isPresent()) {
            throw new BadRequestException("Coupon code already exists");
        }

        Coupon coupon = new Coupon();
        coupon.setCode(request.getCode().toUpperCase());
        coupon.setType(request.getType());
        coupon.setValue(request.getValue());
        coupon.setMinOrderValue(request.getMinOrderValue() != null ? request.getMinOrderValue() : BigDecimal.ZERO);
        coupon.setMaxDiscount(request.getMaxDiscount());
        coupon.setUsageLimitTotal(request.getUsageLimitTotal());
        coupon.setUsageLimitPerUser(request.getUsageLimitPerUser());
        coupon.setStartDate(request.getStartDate());
        coupon.setEndDate(request.getEndDate());
        coupon.setIsActive(true);
        coupon.setCreatedAt(LocalDateTime.now());

        Coupon saved = couponRepository.save(coupon);
        return toResponse(saved);
    }

    @Transactional
    public CouponResponse setActiveStatus(Long id, boolean active) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon not found"));

        coupon.setIsActive(active);
        Coupon updated = couponRepository.save(coupon);
        return toResponse(updated);
    }

    @Transactional(readOnly = true)
    public List<CouponResponse> getAll() {
        return couponRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Validates a coupon code against a user and order subtotal.
     * Returns the discount amount to apply. Throws if invalid.
     */
    @Transactional(readOnly = true)
    public BigDecimal validateAndCalculateDiscount(String code, User user, BigDecimal subtotal) {

        Coupon coupon = couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new BadRequestException("Invalid coupon code"));

        if (!coupon.getIsActive()) {
            throw new BadRequestException("This coupon is no longer active");
        }

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartDate()) || now.isAfter(coupon.getEndDate())) {
            throw new BadRequestException("This coupon has expired or is not yet valid");
        }

        if (subtotal.compareTo(coupon.getMinOrderValue()) < 0) {
            throw new BadRequestException("Order does not meet the minimum value for this coupon: ₹" + coupon.getMinOrderValue());
        }

        if (coupon.getUsageLimitTotal() != null) {
            long totalUsed = couponRedemptionRepository.findByCoupon(coupon).size();
            if (totalUsed >= coupon.getUsageLimitTotal()) {
                throw new BadRequestException("This coupon has reached its usage limit");
            }
        }

        if (coupon.getUsageLimitPerUser() != null) {
            long userUsed = couponRedemptionRepository.findByCouponAndUser(coupon, user).size();
            if (userUsed >= coupon.getUsageLimitPerUser()) {
                throw new BadRequestException("You have already used this coupon the maximum number of times");
            }
        }

        BigDecimal discount;
        if ("PERCENT".equals(coupon.getType())) {
            discount = subtotal.multiply(coupon.getValue()).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        } else {
            discount = coupon.getValue();
        }

        if (coupon.getMaxDiscount() != null && discount.compareTo(coupon.getMaxDiscount()) > 0) {
            discount = coupon.getMaxDiscount();
        }

        if (discount.compareTo(subtotal) > 0) {
            discount = subtotal;
        }

        return discount;
    }

    @Transactional
    public void recordRedemption(Coupon coupon, User user, Order order) {
        CouponRedemption redemption = new CouponRedemption();
        redemption.setCoupon(coupon);
        redemption.setUser(user);
        redemption.setOrder(order);
        redemption.setRedeemedAt(LocalDateTime.now());
        couponRedemptionRepository.save(redemption);
    }

    @Transactional(readOnly = true)
    public Coupon getCouponEntityByCode(String code) {
        return couponRepository.findByCode(code.toUpperCase())
                .orElseThrow(() -> new BadRequestException("Invalid coupon code"));
    }

    private CouponResponse toResponse(Coupon coupon) {
        return new CouponResponse(
                coupon.getId(),
                coupon.getCode(),
                coupon.getType(),
                coupon.getValue(),
                coupon.getMinOrderValue(),
                coupon.getMaxDiscount(),
                coupon.getUsageLimitTotal(),
                coupon.getUsageLimitPerUser(),
                coupon.getStartDate(),
                coupon.getEndDate(),
                coupon.getIsActive()
        );
    }
}