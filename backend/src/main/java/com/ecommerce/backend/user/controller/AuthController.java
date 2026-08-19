package com.ecommerce.backend.user.controller;

import com.ecommerce.backend.user.dto.AuthResponse;
import com.ecommerce.backend.user.dto.LoginRequest;
import com.ecommerce.backend.user.dto.RegisterRequest;
import com.ecommerce.backend.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.ecommerce.backend.user.dto.UpdateProfileRequest;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @GetMapping("/profile")
    public ResponseEntity<AuthResponse> getProfile(Authentication authentication) {
        return ResponseEntity.ok(authService.getProfile(authentication.getName()));
    }

    @PutMapping("/profile")
    public ResponseEntity<AuthResponse> updateProfile(Authentication authentication,
                                                      @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(authService.updateProfile(authentication.getName(), request));
    }




    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}