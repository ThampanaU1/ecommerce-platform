package com.ecommerce.backend.catalog.repository;

import com.ecommerce.backend.catalog.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    List<Banner> findByIsActiveTrueOrderByDisplayOrderAsc();
}