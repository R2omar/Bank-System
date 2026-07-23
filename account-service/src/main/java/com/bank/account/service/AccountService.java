package com.bank.account.service;

import com.bank.account.dto.AccountRequest;
import com.bank.account.dto.AccountResponse;
import com.bank.account.dto.TransferRequest;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponse createAccount(AccountRequest request);

    AccountResponse getAccount(UUID accountId);

    List<AccountResponse> getUserAccounts(UUID userId);

    void transfer(TransferRequest request);
}
