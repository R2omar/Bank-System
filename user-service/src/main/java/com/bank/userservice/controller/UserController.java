package com.bank.userservice.controller;

import com.bank.userservice.dto.*;
import com.bank.userservice.security.CustomUserDetails;
import com.bank.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserProfileResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserProfileResponse response = userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable Long userId,
                                                            Authentication authentication) {
        // A user can only view their own profile unless they're an admin
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        boolean isAdmin = principal.getUser().getRole().name().equals("ADMIN");
        boolean isSelf = principal.getUser().getId().equals(userId);

        if (!isAdmin && !isSelf) {
            throw new AccessDeniedException("You are not allowed to view this profile");
        }

        return ResponseEntity.ok(userService.getProfile(userId));
    }
}
