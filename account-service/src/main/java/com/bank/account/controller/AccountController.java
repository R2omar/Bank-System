package com.bank.account.controller;

import com.bank.account.dto.AccountRequest;
import com.bank.account.dto.AccountResponse;
import com.bank.account.dto.TransferRequest;
import com.bank.account.dto.TransferResponse;
import com.bank.account.service.AccountService;
import com.bank.account.service.LogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
@RestController
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final LogService logService;

    @PostMapping("/accounts")
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody AccountRequest request) {

        logService.logRequest(request);

        AccountResponse response = accountService.createAccount(request);

        logService.logResponse(response);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable UUID accountId) {

        logService.logRequest(accountId);

        AccountResponse response = accountService.getAccount(accountId);

        logService.logResponse(response);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<List<AccountResponse>> getUserAccounts(@PathVariable UUID userId) {

        logService.logRequest(userId);

        List<AccountResponse> response = accountService.getUserAccounts(userId);

        logService.logResponse(response);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/accounts/transfer")
    public ResponseEntity<TransferResponse> transfer(
            @Valid @RequestBody TransferRequest request) {

        logService.logRequest(request);

        accountService.transfer(request);

        TransferResponse response =
                new TransferResponse("Account updated successfully.");

        logService.logResponse(response);

        return ResponseEntity.ok(response);
    }
}