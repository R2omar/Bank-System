package com.bank.account.service;

import com.bank.account.dto.AccountRequest;
import com.bank.account.dto.AccountResponse;
import com.bank.account.dto.TransferRequest;
import com.bank.account.entity.Account;
import com.bank.account.entity.AccountStatus;
import com.bank.account.exception.AccountNotFoundException;
import com.bank.account.exception.InactiveAccountException;
import com.bank.account.exception.InsufficientFundsException;
import com.bank.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        Account account = Account.builder()
                .userId(request.getUserId())
                .accountNumber(generateAccountNumber())
                .accountType(request.getAccountType())
                .balance(request.getBalance())
                .status(AccountStatus.ACTIVE)
                .lastTransactionAt(LocalDateTime.now())
                .build();

        account = accountRepository.save(account);
        return mapToResponse(account);
    }

    @Override
    public AccountResponse getAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found."));
        return mapToResponse(account);
    }

    @Override
    public List<AccountResponse> getUserAccounts(UUID userId) {
        List<Account> accounts = accountRepository.findByUserId(userId);
        if (accounts.isEmpty()) {
            throw new AccountNotFoundException("No accounts found for user ID " + userId + ".");
        }
        return accounts.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void transfer(TransferRequest request) {
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new IllegalArgumentException("Sender and receiver accounts must be different.");
        }

        Account fromAccount = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + request.getFromAccountId() + " not found."));

        Account toAccount = accountRepository.findById(request.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + request.getToAccountId() + " not found."));

        if (fromAccount.getStatus() == AccountStatus.INACTIVE) {
            throw new InactiveAccountException("Sender account is inactive.");
        }
        if (toAccount.getStatus() == AccountStatus.INACTIVE) {
            throw new InactiveAccountException("Receiver account is inactive.");
        }

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException("Insufficient funds.");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        LocalDateTime now = LocalDateTime.now();
        fromAccount.setLastTransactionAt(now);
        toAccount.setLastTransactionAt(now);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    private String generateAccountNumber() {
        return String.format("%010d", (long) (Math.random() * 10000000000L));
    }

    private AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .accountId(account.getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .build();
    }
}
