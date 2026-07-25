package com.bank.transaction.scheduler;

import com.bank.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InterestScheduler {

    private static final Logger log = LoggerFactory.getLogger(InterestScheduler.class);
    private final TransactionService transactionService;

    @Scheduled(cron = "0 0 0 * * *") // Runs daily at midnight
    public void scheduleDailyInterest() {
        log.info("Running scheduled job: Calculate and credit daily interest");
        try {
            transactionService.processDailyInterest();
            log.info("Daily interest processing completed successfully.");
        } catch (Exception e) {
            log.error("Error occurred during daily interest processing: ", e);
        }
    }
}
