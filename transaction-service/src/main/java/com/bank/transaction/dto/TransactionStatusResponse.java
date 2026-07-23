package com.bank.transaction.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TransactionStatusResponse {
    private UUID transactionId;
    private String status;
    private LocalDateTime timestamp;
}
