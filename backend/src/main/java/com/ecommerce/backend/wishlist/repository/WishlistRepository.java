package com.ecommerce.backend.wishlist.repository;

import com.ecommerce.backend.catalog.entity.Product;
import com.ecommerce.backend.user.entity.User;
import com.ecommerce.backend.wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    List<Wishlist> findByUser(User user);

    Optional<Wishlist> findByUserAndProduct(User user, Product product);

    boolean existsByUserAndProduct(User user, Product product);
}