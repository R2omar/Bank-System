package com.bank.transaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ExecutionRequest {
    @NotNull(message = "transactionId is required")
    private UUID transactionId;
}
