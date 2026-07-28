package com.bank.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class RegisterResponse {
    private UUID userId;
    private String username;
    private String message;
}