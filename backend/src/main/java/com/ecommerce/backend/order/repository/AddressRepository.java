package com.ecommerce.backend.order.repository;

import com.ecommerce.backend.order.entity.Address;
import com.ecommerce.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUser(User user);
}