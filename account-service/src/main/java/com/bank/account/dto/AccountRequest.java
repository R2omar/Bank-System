package com.bank.account.dto;

import com.bank.account.entity.AccountType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AccountRequest {
    @NotNull(message = "userId is required")
    private UUID userId;

    @NotNull(message = "accountType is required")
    private AccountType accountType;

    @NotNull(message = "balance is required")
    @PositiveOrZero(message = "initial balance cannot be negative")
    private BigDecimal balance;
}
