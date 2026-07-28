package com.bank.bff.client;

import com.bank.bff.dto.TransactionResponse;
import com.bank.bff.exception.DownstreamServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionClient {

    private final WebClient webClient;

    public TransactionClient(WebClient.Builder builder,
                             @Value("${services.transaction.url}") String transactionServiceUrl) {

        this.webClient = builder
                .baseUrl(transactionServiceUrl)
                .build();
    }

    public Mono<List<TransactionResponse>> getTransactions(UUID accountId) {

        return webClient
                .get()
                .uri("/accounts/{accountId}/transactions", accountId)
                .retrieve()

                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(
                                new DownstreamServiceException("Transaction Service is unavailable"))
                )

                .bodyToFlux(TransactionResponse.class)
                .collectList()

                .timeout(Duration.ofSeconds(5));
    }

}