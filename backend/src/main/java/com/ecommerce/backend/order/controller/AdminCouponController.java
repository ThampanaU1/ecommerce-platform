package com.ecommerce.backend.order.controller;

import com.ecommerce.backend.order.dto.CouponRequest;
import com.ecommerce.backend.order.dto.CouponResponse;
import com.ecommerce.backend.order.service.CouponService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/coupons")
@RequiredArgsConstructor
public class AdminCouponController {

    private final CouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponse> create(@Valid @RequestBody CouponRequest request) {
        return ResponseEntity.ok(couponService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CouponResponse>> getAll() {
        return ResponseEntity.ok(couponService.getAll());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CouponResponse> setStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        boolean active = Boolean.TRUE.equals(body.get("active"));
        return ResponseEntity.ok(couponService.setActiveStatus(id, active));
    }
}