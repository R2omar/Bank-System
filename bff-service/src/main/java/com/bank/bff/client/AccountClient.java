package com.bank.bff.client;

import com.bank.bff.dto.AccountResponse;
import com.bank.bff.exception.DownstreamServiceException;
import com.bank.bff.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class AccountClient {

    private final WebClient webClient;

    public AccountClient(WebClient.Builder builder,
                         @Value("${services.account.url}") String accountServiceUrl) {

        this.webClient = builder
                .baseUrl(accountServiceUrl)
                .build();
    }

    public Mono<List<AccountResponse>> getUserAccounts(UUID userId) {

        return webClient
                .get()
                .uri("/users/{userId}/accounts", userId)
                .retrieve()

//                .onStatus(
//                        status -> status.value() == 404,
//                        response -> Mono.error(
//                                new ResourceNotFoundException("No accounts found"))
//                )

                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(
                                new DownstreamServiceException("Account Service is unavailable"))
                )

                .bodyToFlux(AccountResponse.class)
                .collectList()


                .onErrorResume(
                        WebClientResponseException.NotFound.class,
                        ex -> Mono.just(Collections.emptyList())
                )

                .timeout(Duration.ofSeconds(5));
    }

}