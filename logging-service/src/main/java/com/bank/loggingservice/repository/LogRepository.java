package com.bank.loggingservice.repository;

import com.bank.loggingservice.entity.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LogEntry, Long> {

}