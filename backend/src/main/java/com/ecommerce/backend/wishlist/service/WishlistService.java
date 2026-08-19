package com.ecommerce.backend.wishlist.service;

import com.ecommerce.backend.catalog.entity.Product;
import com.ecommerce.backend.catalog.repository.InventoryRepository;
import com.ecommerce.backend.catalog.repository.ProductImageRepository;
import com.ecommerce.backend.catalog.repository.ProductPriceRepository;
import com.ecommerce.backend.catalog.repository.ProductRepository;
import com.ecommerce.backend.catalog.entity.ProductImage;
import com.ecommerce.backend.common.exception.BadRequestException;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;
import com.ecommerce.backend.user.entity.User;
import com.ecommerce.backend.user.repository.UserRepository;
import com.ecommerce.backend.wishlist.dto.WishlistResponse;
import com.ecommerce.backend.wishlist.entity.Wishlist;
import com.ecommerce.backend.wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductPriceRepository productPriceRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional
    public WishlistResponse addToWishlist(String userEmail, Long productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (wishlistRepository.existsByUserAndProduct(user, product)) {
            throw new BadRequestException("Product is already in your wishlist");
        }

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setProduct(product);
        wishlist.setAddedAt(LocalDateTime.now());

        Wishlist saved = wishlistRepository.save(wishlist);
        return toResponse(saved);
    }

    @Transactional
    public void removeFromWishlist(String userEmail, Long productId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Wishlist wishlist = wishlistRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new ResourceNotFoundException("Item not in wishlist"));

        wishlistRepository.delete(wishlist);
    }

    @Transactional(readOnly = true)
    public List<WishlistResponse> getMyWishlist(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return wishlistRepository.findByUser(user).stream()
                .map(this::toResponse)
                .toList();
    }

    private WishlistResponse toResponse(Wishlist wishlist) {
        Product product = wishlist.getProduct();

        var priceOpt = productPriceRepository.findByProductAndActiveTrue(product);
        var sellingPrice = priceOpt.map(p -> p.getSellingPrice()).orElse(null);

        var inventoryOpt = inventoryRepository.findByProduct(product);
        boolean inStock = inventoryOpt.map(inv -> inv.getAvailableQuantity() > 0).orElse(false);

        String primaryImageUrl = productImageRepository.findByProduct(product).stream()
                .filter(img -> Boolean.TRUE.equals(img.getIsPrimary()))
                .findFirst()
                .map(ProductImage::getImageUrl)
                .orElse(null);

        return new WishlistResponse(
                wishlist.getId(),
                product.getId(),
                product.getName(),
                product.getSku(),
                sellingPrice,
                primaryImageUrl,
                inStock,
                wishlist.getAddedAt()
        );
    }
}