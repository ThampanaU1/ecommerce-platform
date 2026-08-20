package com.ecommerce.backend.user.controller;

import com.ecommerce.backend.user.dto.AdminUserResponse;
import com.ecommerce.backend.user.dto.UpdateRolesRequest;
import com.ecommerce.backend.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<AdminUserResponse>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<AdminUserResponse> updateRoles(@PathVariable Long id,
                                                         @Valid @RequestBody UpdateRolesRequest request) {
        return ResponseEntity.ok(authService.updateUserRoles(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AdminUserResponse> updateStatus(@PathVariable Long id,
                                                          @RequestBody Map<String, String> body) {
        return ResponseEntity.ok(authService.updateUserStatus(id, body.get("status")));
    }
}