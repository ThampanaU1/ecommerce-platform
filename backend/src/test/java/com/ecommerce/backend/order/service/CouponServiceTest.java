package com.ecommerce.backend.order.service;

import com.ecommerce.backend.common.exception.BadRequestException;
import com.ecommerce.backend.order.entity.Coupon;
import com.ecommerce.backend.order.repository.CouponRedemptionRepository;
import com.ecommerce.backend.order.repository.CouponRepository;
import com.ecommerce.backend.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponRedemptionRepository couponRedemptionRepository;

    @InjectMocks
    private CouponService couponService;

    private Coupon percentCoupon;
    private User user;

    @BeforeEach
    void setUp() {
        percentCoupon = new Coupon();
        percentCoupon.setId(1L);
        percentCoupon.setCode("SAVE10");
        percentCoupon.setType("PERCENT");
        percentCoupon.setValue(new BigDecimal("10"));
        percentCoupon.setMinOrderValue(new BigDecimal("500"));
        percentCoupon.setMaxDiscount(new BigDecimal("300"));
        percentCoupon.setIsActive(true);
        percentCoupon.setStartDate(LocalDateTime.now().minusDays(1));
        percentCoupon.setEndDate(LocalDateTime.now().plusDays(30));

        user = new User();
        user.setId(1L);
    }

    @Test
    void calculatesPercentDiscountCorrectly() {
        when(couponRepository.findByCode("SAVE10")).thenReturn(Optional.of(percentCoupon));

        BigDecimal discount = couponService.validateAndCalculateDiscount("SAVE10", user, new BigDecimal("1999"));

        assertEquals(new BigDecimal("199.90"), discount);
    }

    @Test
    void discountNeverExceedsMaxDiscountCap() {
        when(couponRepository.findByCode("SAVE10")).thenReturn(Optional.of(percentCoupon));

        // 10% of 5000 would be 500, but maxDiscount caps it at 300
        BigDecimal discount = couponService.validateAndCalculateDiscount("SAVE10", user, new BigDecimal("5000"));

        assertEquals(new BigDecimal("300"), discount);
    }

    @Test
    void rejectsOrderBelowMinimumValue() {
        when(couponRepository.findByCode("SAVE10")).thenReturn(Optional.of(percentCoupon));

        assertThrows(BadRequestException.class, () ->
                couponService.validateAndCalculateDiscount("SAVE10", user, new BigDecimal("100"))
        );
    }

    @Test
    void rejectsExpiredCoupon() {
        percentCoupon.setEndDate(LocalDateTime.now().minusDays(1));
        when(couponRepository.findByCode("SAVE10")).thenReturn(Optional.of(percentCoupon));

        assertThrows(BadRequestException.class, () ->
                couponService.validateAndCalculateDiscount("SAVE10", user, new BigDecimal("1999"))
        );
    }

    @Test
    void rejectsInvalidCouponCode() {
        when(couponRepository.findByCode("FAKE")).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () ->
                couponService.validateAndCalculateDiscount("FAKE", user, new BigDecimal("1999"))
        );
    }
}