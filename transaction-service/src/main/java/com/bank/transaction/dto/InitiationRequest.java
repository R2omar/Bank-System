package com.bank.transaction.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class InitiationRequest {
    @NotNull(message = "fromAccountId is required")
    private UUID fromAccountId;

    @NotNull(message = "toAccountId is required")
    private UUID toAccountId;

    @NotNull(message = "amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private String description;
}
