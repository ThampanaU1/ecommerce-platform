package com.ecommerce.backend.order.repository;

import com.ecommerce.backend.order.entity.Coupon;
import com.ecommerce.backend.order.entity.CouponRedemption;
import com.ecommerce.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, Long> {

    List<CouponRedemption> findByCoupon(Coupon coupon);

    List<CouponRedemption> findByCouponAndUser(Coupon coupon, User user);
}