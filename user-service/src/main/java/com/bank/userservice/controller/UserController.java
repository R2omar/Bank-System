package com.bank.userservice.controller;

import com.bank.userservice.dto.*;
import com.bank.userservice.service.UserService;
import jakarta.validation.Valid;
import com.bank.userservice.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;
    private final LogService logService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        logService.logRequest(request);

        RegisterResponse response = userService.register(request);

        logService.logResponse(response);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        logService.logRequest(request);

        LoginResponse response = userService.login(request);

        logService.logResponse(response);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileResponse> getProfile(
            @PathVariable UUID userId) {

        logService.logRequest(userId);

        UserProfileResponse response = userService.getProfile(userId);

        logService.logResponse(response);

        return ResponseEntity.ok(response);
    }
}