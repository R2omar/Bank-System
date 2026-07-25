package com.bank.transaction.service.impl;

import com.bank.transaction.client.AccountClient;
import com.bank.transaction.dto.*;
import com.bank.transaction.entity.Transaction;
import com.bank.transaction.entity.TransactionStatus;
import com.bank.transaction.exception.TransactionNotFoundException;
import com.bank.transaction.repository.TransactionRepository;
import com.bank.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountClient accountClient;

    @Override
    public TransactionStatusResponse initiateTransfer(InitiationRequest request) {
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new IllegalArgumentException("Invalid 'from' or 'to' account ID.");
        }

        Transaction transaction = Transaction.builder()
                .fromAccountId(request.getFromAccountId())
                .toAccountId(request.getToAccountId())
                .amount(request.getAmount())
                .description(request.getDescription())
                .status(TransactionStatus.INITIATED)
                .timestamp(LocalDateTime.now())
                .build();

        transaction = transactionRepository.save(transaction);

        return TransactionStatusResponse.builder()
                .transactionId(transaction.getId())
                .status("Initiated")
                .timestamp(transaction.getTimestamp())
                .build();
    }

    @Override
    public TransactionStatusResponse executeTransfer(ExecutionRequest request) {
        Transaction transaction = transactionRepository.findById(request.getTransactionId())
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found."));

        if (transaction.getStatus() != TransactionStatus.INITIATED) {
            throw new IllegalArgumentException("Transaction is not in INITIATED state.");
        }

        UUID virtualBankAccountId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        if (!transaction.getFromAccountId().equals(virtualBankAccountId)) {
            try {
                AccountResponse fromAccount = accountClient.getAccount(transaction.getFromAccountId()).getBody();
                if (fromAccount == null) {
                    transaction.setStatus(TransactionStatus.FAILED);
                    transactionRepository.save(transaction);
                    throw new IllegalArgumentException("Invalid 'from' or 'to' account ID.");
                }
                if (fromAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
                    transaction.setStatus(TransactionStatus.FAILED);
                    transactionRepository.save(transaction);
                    throw new IllegalArgumentException("Insufficient funds.");
                }
            } catch (IllegalArgumentException e) {
                throw e;
            } catch (Exception e) {
                transaction.setStatus(TransactionStatus.FAILED);
                transactionRepository.save(transaction);
                throw new IllegalArgumentException("Invalid 'from' or 'to' account ID.");
            }
        }

        try {
            AccountTransferRequest transferRequest = AccountTransferRequest.builder()
                    .fromAccountId(transaction.getFromAccountId())
                    .toAccountId(transaction.getToAccountId())
                    .amount(transaction.getAmount())
                    .build();

            accountClient.transfer(transferRequest);
            transaction.setStatus(TransactionStatus.SUCCESS);
        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);
            String message = e.getMessage() != null && e.getMessage().contains("Insufficient") ? "Insufficient funds."
                    : "Invalid 'from' or 'to' account ID.";
            throw new IllegalArgumentException(message);
        }

        transaction = transactionRepository.save(transaction);

        return TransactionStatusResponse.builder()
                .transactionId(transaction.getId())
                .status("Success")
                .timestamp(transaction.getTimestamp())
                .build();
    }

    @Override
    public List<TransactionHistoryResponse> getAccountTransactions(UUID accountId) {
        List<Transaction> transactions = transactionRepository.findTransactionsByAccountId(accountId);
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException("No transactions found for account ID " + accountId + ".");
        }

        return transactions.stream().map(t -> {
            String deliveryStatus = "SENT";
            t.setAmount(t.getAmount().negate());
            if (t.getToAccountId().equals(accountId)) {
                deliveryStatus = "DELIVERED";
                t.setAmount(t.getAmount().negate());
            }
            return TransactionHistoryResponse.builder()
                    .transactionId(t.getId())
                    .fromAccountId(t.getFromAccountId())
                    .toAccountId(t.getToAccountId())
                    .amount(t.getAmount())
                    .description(t.getDescription())
                    .timestamp(t.getTimestamp())
                    .deliveryStatus(deliveryStatus)
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public void processDailyInterest() {
        // Fetch active savings accounts
        List<ActiveSavingsAccountResponse> savingsAccounts = accountClient.getActiveSavingsAccounts().getBody();
        if (savingsAccounts == null || savingsAccounts.isEmpty()) {
            return;
        }

        UUID virtualBankAccountId = UUID.fromString("00000000-0000-0000-0000-000000000000"); // Example ID for virtual
                                                                                             // bank

        for (ActiveSavingsAccountResponse account : savingsAccounts) {
            BigDecimal interest = account.getBalance().multiply(new BigDecimal("0.05")); // 5% interest

            InitiationRequest initiationRequest = new InitiationRequest();
            initiationRequest.setFromAccountId(virtualBankAccountId);
            initiationRequest.setToAccountId(account.getAccountId());
            initiationRequest.setAmount(interest);
            initiationRequest.setDescription("Daily Interest Credit");

            TransactionStatusResponse initiated = initiateTransfer(initiationRequest);

            ExecutionRequest executionRequest = new ExecutionRequest();
            executionRequest.setTransactionId(initiated.getTransactionId());
            try {
                executeTransfer(executionRequest);
            } catch (Exception e) {
                // Log and continue
                System.err.println("Failed to execute interest transfer for account: " + account.getAccountId());
            }
        }
    }
}
