package com.bank.transaction.client;

import com.bank.transaction.dto.AccountTransferRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service", url = "${account.service.url}")
public interface AccountClient {

    @PutMapping("/accounts/transfer")
    ResponseEntity<Object> transfer(@RequestBody AccountTransferRequest request);

    @GetMapping("/accounts/active-savings")
    ResponseEntity<java.util.List<com.bank.transaction.dto.ActiveSavingsAccountResponse>> getActiveSavingsAccounts();

    @GetMapping("/accounts/{accountId}")
    ResponseEntity<com.bank.transaction.dto.AccountResponse> getAccount(@org.springframework.web.bind.annotation.PathVariable("accountId") java.util.UUID accountId);
}
