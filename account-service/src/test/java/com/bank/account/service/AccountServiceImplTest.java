package com.bank.account.service;

import com.bank.account.dto.AccountRequest;
import com.bank.account.dto.AccountResponse;
import com.bank.account.dto.TransferRequest;
import com.bank.account.entity.Account;
import com.bank.account.entity.AccountStatus;
import com.bank.account.entity.AccountType;
import com.bank.account.exception.AccountNotFoundException;
import com.bank.account.exception.InactiveAccountException;
import com.bank.account.exception.InsufficientFundsException;
import com.bank.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account activeAccount1;
    private Account activeAccount2;
    private Account inactiveAccount;

    @BeforeEach
    void setUp() {
        activeAccount1 = Account.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .accountNumber("1234567890")
                .accountType(AccountType.SAVINGS)
                .balance(new BigDecimal("1000.00"))
                .status(AccountStatus.ACTIVE)
                .lastTransactionAt(LocalDateTime.now())
                .build();

        activeAccount2 = Account.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .accountNumber("0987654321")
                .accountType(AccountType.CHECKING)
                .balance(new BigDecimal("500.00"))
                .status(AccountStatus.ACTIVE)
                .lastTransactionAt(LocalDateTime.now())
                .build();

        inactiveAccount = Account.builder()
                .id(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .accountNumber("1122334455")
                .accountType(AccountType.SAVINGS)
                .balance(new BigDecimal("100.00"))
                .status(AccountStatus.INACTIVE)
                .lastTransactionAt(LocalDateTime.now().minusDays(2))
                .build();
    }

    @Test
    void testCreateAccount_Success() {
        AccountRequest request = new AccountRequest();
        request.setUserId(UUID.randomUUID());
        request.setAccountType(AccountType.SAVINGS);
        request.setInitialBalance(new BigDecimal("200.00"));

        when(accountRepository.save(any(Account.class))).thenAnswer(i -> {
            Account acc = i.getArgument(0);
            acc.setId(UUID.randomUUID());
            return acc;
        });

        AccountResponse response = accountService.createAccount(request);

        assertNotNull(response);
        assertNotNull(response.getAccountId());
        assertEquals("Account created successfully.", response.getMessage());
        assertNotNull(response.getAccountNumber());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testGetAccount_Success() {
        when(accountRepository.findById(activeAccount1.getId())).thenReturn(Optional.of(activeAccount1));

        AccountResponse response = accountService.getAccount(activeAccount1.getId());

        assertNotNull(response);
        assertEquals(activeAccount1.getId(), response.getAccountId());
    }

    @Test
    void testGetAccount_NotFound() {
        UUID id = UUID.randomUUID();
        when(accountRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount(id));
    }

    @Test
    void testGetUserAccounts_Success() {
        when(accountRepository.findByUserId(activeAccount1.getUserId())).thenReturn(List.of(activeAccount1));

        List<AccountResponse> responses = accountService.getUserAccounts(activeAccount1.getUserId());

        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(activeAccount1.getId(), responses.get(0).getAccountId());
    }

    @Test
    void testGetUserAccounts_NotFound() {
        UUID id = UUID.randomUUID();
        when(accountRepository.findByUserId(id)).thenReturn(List.of());

        assertThrows(AccountNotFoundException.class, () -> accountService.getUserAccounts(id));
    }

    @Test
    void testTransfer_Success() {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(activeAccount1.getId());
        request.setToAccountId(activeAccount2.getId());
        request.setAmount(new BigDecimal("100.00"));

        when(accountRepository.findById(activeAccount1.getId())).thenReturn(Optional.of(activeAccount1));
        when(accountRepository.findById(activeAccount2.getId())).thenReturn(Optional.of(activeAccount2));

        accountService.transfer(request);

        assertEquals(new BigDecimal("900.00"), activeAccount1.getBalance());
        assertEquals(new BigDecimal("600.00"), activeAccount2.getBalance());
        verify(accountRepository, times(2)).save(any(Account.class));
    }

    @Test
    void testTransfer_InsufficientFunds() {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(activeAccount1.getId());
        request.setToAccountId(activeAccount2.getId());
        request.setAmount(new BigDecimal("2000.00"));

        when(accountRepository.findById(activeAccount1.getId())).thenReturn(Optional.of(activeAccount1));
        when(accountRepository.findById(activeAccount2.getId())).thenReturn(Optional.of(activeAccount2));

        assertThrows(InsufficientFundsException.class, () -> accountService.transfer(request));
    }

    @Test
    void testTransfer_InactiveAccount() {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(inactiveAccount.getId());
        request.setToAccountId(activeAccount2.getId());
        request.setAmount(new BigDecimal("50.00"));

        when(accountRepository.findById(inactiveAccount.getId())).thenReturn(Optional.of(inactiveAccount));
        when(accountRepository.findById(activeAccount2.getId())).thenReturn(Optional.of(activeAccount2));

        assertThrows(InactiveAccountException.class, () -> accountService.transfer(request));
    }
}
