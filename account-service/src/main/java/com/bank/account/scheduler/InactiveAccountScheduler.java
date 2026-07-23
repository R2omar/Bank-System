package com.bank.account.scheduler;

import com.bank.account.entity.Account;
import com.bank.account.entity.AccountStatus;
import com.bank.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InactiveAccountScheduler {

    private static final Logger log = LoggerFactory.getLogger(InactiveAccountScheduler.class);
    private final AccountRepository accountRepository;

    @Scheduled(cron = "0 0 * * * *") // Runs every hour at minute 0
    @Transactional
    public void markInactiveAccounts() {
        log.info("Running scheduled job: Inactivate Stale Accounts");
        
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        
        List<Account> staleAccounts = accountRepository.findByStatusAndLastTransactionAtBefore(
                AccountStatus.ACTIVE, twentyFourHoursAgo);

        if (!staleAccounts.isEmpty()) {
            log.info("Found {} stale active accounts to mark as INACTIVE.", staleAccounts.size());
            
            for (Account account : staleAccounts) {
                account.setStatus(AccountStatus.INACTIVE);
            }
            
            accountRepository.saveAll(staleAccounts);
            log.info("Successfully marked {} accounts as INACTIVE.", staleAccounts.size());
        } else {
            log.info("No stale active accounts found.");
        }
    }
}
