package com.ecommerce.backend.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {

    private Long userId;
    private String name;
    private String email;
    private Set<String> roles;
    private String token;
}