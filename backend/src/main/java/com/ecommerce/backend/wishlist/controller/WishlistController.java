package com.ecommerce.backend.wishlist.controller;

import com.ecommerce.backend.wishlist.dto.WishlistResponse;
import com.ecommerce.backend.wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getMyWishlist(Authentication authentication) {
        return ResponseEntity.ok(wishlistService.getMyWishlist(authentication.getName()));
    }

    @PostMapping("/{productId}")
    public ResponseEntity<WishlistResponse> addToWishlist(Authentication authentication,
                                                          @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.addToWishlist(authentication.getName(), productId));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromWishlist(Authentication authentication,
                                                   @PathVariable Long productId) {
        wishlistService.removeFromWishlist(authentication.getName(), productId);
        return ResponseEntity.noContent().build();
    }
}