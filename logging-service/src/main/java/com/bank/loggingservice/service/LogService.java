package com.bank.loggingservice.service;

import com.bank.loggingservice.dto.LogMessage;
import com.bank.loggingservice.entity.LogEntry;
import com.bank.loggingservice.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogService {

    private final LogRepository logRepository;

    public void save(LogMessage message) {

        LogEntry logEntry = LogEntry.builder()
                .message(message.getMessage())
                .messageType(message.getMessageType())
                .dateTime(message.getDateTime())
                .build();

        logRepository.save(logEntry);
    }

}