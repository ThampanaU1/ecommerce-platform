package com.ecommerce.backend.catalog.service;

import com.ecommerce.backend.catalog.dto.BannerRequest;
import com.ecommerce.backend.catalog.dto.BannerResponse;
import com.ecommerce.backend.catalog.entity.Banner;
import com.ecommerce.backend.catalog.repository.BannerRepository;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    @Transactional
    public BannerResponse create(BannerRequest request) {
        Banner banner = new Banner();
        banner.setTitle(request.getTitle());
        banner.setImageUrl(request.getImageUrl());
        banner.setLinkUrl(request.getLinkUrl());
        banner.setDisplayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0);
        banner.setIsActive(true);
        banner.setCreatedAt(LocalDateTime.now());

        Banner saved = bannerRepository.save(banner);
        return toResponse(saved);
    }

    @Transactional
    public BannerResponse setStatus(Long id, boolean active) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner not found"));
        banner.setIsActive(active);
        return toResponse(bannerRepository.save(banner));
    }

    @Transactional
    public void delete(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Banner not found"));
        bannerRepository.delete(banner);
    }

    @Transactional(readOnly = true)
    public List<BannerResponse> getAllAdmin() {
        return bannerRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<BannerResponse> getActiveBanners() {
        return bannerRepository.findByIsActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::toResponse).toList();
    }

    private BannerResponse toResponse(Banner banner) {
        return new BannerResponse(
                banner.getId(), banner.getTitle(), banner.getImageUrl(),
                banner.getLinkUrl(), banner.getDisplayOrder(), banner.getIsActive()
        );
    }
}