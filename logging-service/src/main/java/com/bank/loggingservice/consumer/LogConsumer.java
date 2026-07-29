package com.bank.loggingservice.consumer;

import com.bank.loggingservice.dto.LogMessage;
import com.bank.loggingservice.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class LogConsumer {

    private final LogService logService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "virtual-bank-logs",
            groupId = "logging-group"
    )
    public void consume(String json) {

        try {

            LogMessage message = objectMapper.readValue(json, LogMessage.class);

            System.out.println("Received: " + message);

            logService.save(message);

        } catch (JacksonException e) {
            throw new RuntimeException("Failed to deserialize log message", e);
        }

    }
}