package com.bank.transaction.service;

import com.bank.transaction.dto.ExecutionRequest;
import com.bank.transaction.dto.InitiationRequest;
import com.bank.transaction.dto.TransactionHistoryResponse;
import com.bank.transaction.dto.TransactionStatusResponse;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionStatusResponse initiateTransfer(InitiationRequest request);
    TransactionStatusResponse executeTransfer(ExecutionRequest request);
    List<TransactionHistoryResponse> getAccountTransactions(UUID accountId);
    void processDailyInterest();
}
