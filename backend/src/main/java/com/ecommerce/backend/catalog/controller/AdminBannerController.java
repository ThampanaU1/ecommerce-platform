package com.ecommerce.backend.catalog.controller;

import com.ecommerce.backend.catalog.dto.BannerRequest;
import com.ecommerce.backend.catalog.dto.BannerResponse;
import com.ecommerce.backend.catalog.service.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/banners")
@RequiredArgsConstructor
public class AdminBannerController {

    private final BannerService bannerService;

    @GetMapping
    public ResponseEntity<List<BannerResponse>> getAll() {
        return ResponseEntity.ok(bannerService.getAllAdmin());
    }

    @PostMapping
    public ResponseEntity<BannerResponse> create(@Valid @RequestBody BannerRequest request) {
        return ResponseEntity.ok(bannerService.create(request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BannerResponse> setStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> body) {
        boolean active = Boolean.TRUE.equals(body.get("active"));
        return ResponseEntity.ok(bannerService.setStatus(id, active));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bannerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}