package com.ecommerce.backend.user.service;

import com.ecommerce.backend.common.security.JwtUtil;
import com.ecommerce.backend.user.dto.AuthResponse;
import com.ecommerce.backend.user.dto.LoginRequest;
import com.ecommerce.backend.user.dto.RegisterRequest;
import com.ecommerce.backend.user.entity.Role;
import com.ecommerce.backend.user.entity.User;
import com.ecommerce.backend.user.repository.RoleRepository;
import com.ecommerce.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ecommerce.backend.user.dto.UpdateProfileRequest;
import com.ecommerce.backend.common.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;


    @Transactional
    public AuthResponse updateProfile(String userEmail, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setName(request.getName());
        user.setUpdatedAt(LocalDateTime.now());
        User updated = userRepository.save(user);

        return buildAuthResponse(updated, null);
    }

    @Transactional(readOnly = true)
    public AuthResponse getProfile(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return buildAuthResponse(user, null);
    }




    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalStateException("Email is already registered");
        }

        Role customerRole = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new IllegalStateException("Default role CUSTOMER not found"));

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setStatus("ACTIVE");
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        Set<Role> roles = new HashSet<>();
        roles.add(customerRole);
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

        return buildAuthResponse(
                savedUser,
                jwtUtil.generateToken(
                        savedUser.getId(),
                        savedUser.getEmail(),
                        roleNamesFor(savedUser)
                )
        );
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalStateException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalStateException("Invalid email or password");
        }

        return buildAuthResponse(
                user,
                jwtUtil.generateToken(
                        user.getId(),
                        user.getEmail(),
                        roleNamesFor(user)
                )
        );
    }

    private Set<String> roleNamesFor(User user) {
        return user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    private AuthResponse buildAuthResponse(User user, String token) {
        Set<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new AuthResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                roleNames,
                token
        );
    }
}