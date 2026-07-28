package com.bank.bff.client;

import com.bank.bff.dto.UserProfileResponse;
import com.bank.bff.exception.DownstreamServiceException;
import com.bank.bff.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

@Service
public class UserClient {

    private final WebClient webClient;

    public UserClient(WebClient.Builder builder,
                      @Value("${services.user.url}") String userServiceUrl) {

        this.webClient = builder
                .baseUrl(userServiceUrl)
                .build();
    }

    public Mono<UserProfileResponse> getUserProfile(UUID userId) {

        return webClient
                .get()
                .uri("/users/{userId}/profile", userId)
                .retrieve()

                .onStatus(
                        status -> status.value() == 404,
                        response -> Mono.error(
                                new ResourceNotFoundException("User not found"))
                )

                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> Mono.error(
                                new DownstreamServiceException("User Service is unavailable"))
                )

                .bodyToMono(UserProfileResponse.class)

                .timeout(Duration.ofSeconds(5));
    }

}