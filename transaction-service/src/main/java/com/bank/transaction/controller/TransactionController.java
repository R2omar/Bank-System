package com.bank.transaction.controller;

import com.bank.transaction.dto.ExecutionRequest;
import com.bank.transaction.dto.InitiationRequest;
import com.bank.transaction.dto.TransactionHistoryResponse;
import com.bank.transaction.dto.TransactionStatusResponse;
import com.bank.transaction.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transactions/transfer/initiation")
    public ResponseEntity<TransactionStatusResponse> initiateTransfer(@Valid @RequestBody InitiationRequest request) {
        return ResponseEntity.ok(transactionService.initiateTransfer(request));
    }

    @PostMapping("/transactions/transfer/execution")
    public ResponseEntity<TransactionStatusResponse> executeTransfer(@Valid @RequestBody ExecutionRequest request) {
        return ResponseEntity.ok(transactionService.executeTransfer(request));
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionHistoryResponse>> getAccountTransactions(@PathVariable UUID accountId) {
        return ResponseEntity.ok(transactionService.getAccountTransactions(accountId));
    }
}
