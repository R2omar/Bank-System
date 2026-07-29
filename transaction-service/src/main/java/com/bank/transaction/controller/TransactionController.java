package com.bank.transaction.controller;

import com.bank.transaction.dto.ExecutionRequest;
import com.bank.transaction.dto.InitiationRequest;
import com.bank.transaction.dto.TransactionHistoryResponse;
import com.bank.transaction.dto.TransactionStatusResponse;
import com.bank.transaction.service.LogService;
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
    private final LogService logService;

    @PostMapping("/transactions/transfer/initiation")
    public ResponseEntity<TransactionStatusResponse> initiateTransfer(
            @Valid @RequestBody InitiationRequest request) {

        logService.logRequest(request);

        TransactionStatusResponse response =
                transactionService.initiateTransfer(request);

        logService.logResponse(response);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/transactions/transfer/execution")
    public ResponseEntity<TransactionStatusResponse> executeTransfer(
            @Valid @RequestBody ExecutionRequest request) {

        logService.logRequest(request);

        TransactionStatusResponse response =
                transactionService.executeTransfer(request);

        logService.logResponse(response);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<List<TransactionHistoryResponse>> getAccountTransactions(
            @PathVariable UUID accountId) {

        logService.logRequest(accountId);

        List<TransactionHistoryResponse> response =
                transactionService.getAccountTransactions(accountId);

        logService.logResponse(response);

        return ResponseEntity.ok(response);
    }
}