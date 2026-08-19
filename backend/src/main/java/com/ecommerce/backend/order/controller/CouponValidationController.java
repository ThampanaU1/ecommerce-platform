package com.ecommerce.backend.order.controller;

import com.ecommerce.backend.order.dto.ApplyCouponRequest;
import com.ecommerce.backend.order.service.CouponService;
import com.ecommerce.backend.user.repository.UserRepository;
import com.ecommerce.backend.user.entity.User;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponValidationController {

    private final CouponService couponService;
    private final UserRepository userRepository;

    @PostMapping("/validate")
    public ResponseEntity<Map<String, BigDecimal>> validate(Authentication authentication,
                                                            @Valid @RequestBody ApplyCouponRequest request) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        BigDecimal discount = couponService.validateAndCalculateDiscount(
                request.getCode(), user, request.getOrderSubtotal());

        return ResponseEntity.ok(Map.of("discount", discount));
    }
}