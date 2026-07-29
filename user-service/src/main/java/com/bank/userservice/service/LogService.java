package com.bank.userservice.service;

import com.bank.userservice.dto.LogMessage;
import com.bank.userservice.producer.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogService {

    private final KafkaProducerService kafkaProducerService;
    private final ObjectMapper objectMapper;

    public void logRequest(Object request) {

        try {
            LogMessage log = LogMessage.builder()
                    .message(objectMapper.writeValueAsString(request))
                    .messageType("Request")
                    .dateTime(LocalDateTime.now().toString())
                    .build();

            String json = objectMapper.writeValueAsString(log);

            kafkaProducerService.send(json);

        } catch (JacksonException e) {
            throw new RuntimeException("Failed to serialize request", e);
        }
    }

    public void logResponse(Object response) {

        try {
            LogMessage log = LogMessage.builder()
                    .message(objectMapper.writeValueAsString(response))
                    .messageType("Response")
                    .dateTime(LocalDateTime.now().toString())
                    .build();

            String json = objectMapper.writeValueAsString(log);

            kafkaProducerService.send(json);

        } catch (JacksonException e) {
            throw new RuntimeException("Failed to serialize response", e);
        }
    }
}
