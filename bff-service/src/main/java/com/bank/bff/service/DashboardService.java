package com.bank.bff.service;

import com.bank.bff.client.AccountClient;
import com.bank.bff.client.TransactionClient;
import com.bank.bff.client.UserClient;
import com.bank.bff.dto.AccountResponse;
import com.bank.bff.dto.DashboardAccountResponse;
import com.bank.bff.dto.DashboardResponse;
import com.bank.bff.dto.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserClient userClient;
    private final AccountClient accountClient;
    private final TransactionClient transactionClient;


    public Mono<DashboardResponse> getDashboard(UUID userId) {

        Mono<UserProfileResponse> profileMono =
                userClient.getUserProfile(userId);

        Mono<List<AccountResponse>> accountsMono =
                accountClient.getUserAccounts(userId);

        return Mono.zip(profileMono, accountsMono)
                .flatMap(tuple -> {
                    UserProfileResponse profile = tuple.getT1();
                    List<AccountResponse> accounts = tuple.getT2();
                    return Flux.fromIterable(accounts)
                            .flatMap(account ->
                                    transactionClient
                                            .getTransactions(account.getAccountId())
                                            .map(transactions ->
                                                    DashboardAccountResponse.builder()
                                                            .accountId(account.getAccountId())
                                                            .accountNumber(account.getAccountNumber())
                                                            .accountType(account.getAccountType())
                                                            .balance(account.getBalance())
                                                            .transactions(transactions)
                                                            .build()
                                            )
                            )
                            .collectList()
                            .map(dashboardAccounts ->
                                    DashboardResponse.builder()
                                            .userId(profile.getUserId())
                                            .username(profile.getUsername())
                                            .email(profile.getEmail())
                                            .firstName(profile.getFirstName())
                                            .lastName(profile.getLastName())
                                            .accounts(dashboardAccounts)
                                            .build()
                            );
                });

    }

}
