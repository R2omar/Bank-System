package com.bank.transaction.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ActiveSavingsAccountResponse {
    private UUID accountId;
    private BigDecimal balance;
}
