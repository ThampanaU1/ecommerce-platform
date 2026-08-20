package com.ecommerce.backend.user.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateRolesRequest {

    @NotEmpty(message = "At least one role is required")
    private Set<String> roles;
}