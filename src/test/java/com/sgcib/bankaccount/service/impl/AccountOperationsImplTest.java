package com.sgcib.bankaccount.service.impl;

import com.sgcib.bankaccount.exception.AccountNotFoundException;
import com.sgcib.bankaccount.exception.InsufficientFundsException;
import com.sgcib.bankaccount.model.BankAccount;
import com.sgcib.bankaccount.model.Client;
import com.sgcib.bankaccount.model.OperationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountOperationsImplTest {

    @InjectMocks
    private AccountOperationsImpl accountOperations;

    @Test
    void createAccount_ShouldCreateNewAccount() {
        // Given
        Client client = Client.builder()
                .firstName("Will")
                .lastName("Smith")
                .build();

        // When
        BankAccount account = accountOperations.createAccount(client);

        // Then
        assertNotNull(account);
        assertEquals(client, account.getClient());
        assertNotNull(account.getAccountNum());
        assertTrue(account.getOperations().isEmpty());
    }

    @Test
    void deposit_ShouldIncreaseBalance() {
        // Given
        BankAccount account = createTestAccount();
        BigDecimal depositAmount = new BigDecimal("100.00");

        // When
        accountOperations.deposit(account.getAccountNum(), depositAmount);

        // Then
        assertEquals(depositAmount, account.getBalance());
        assertEquals(1, account.getOperations().size());
        assertEquals(OperationType.DEPOSIT, account.getOperations().get(0).getType());
    }

    @Test
    void withdraw_ShouldDecreaseBalance() {
        // Given
        BankAccount account = createTestAccount();
        accountOperations.deposit(account.getAccountNum(), new BigDecimal("100.00"));
        BigDecimal withdrawAmount = new BigDecimal("50.00");

        // When
        accountOperations.withdraw(account.getAccountNum(), withdrawAmount);

        // Then
        assertEquals(new BigDecimal("50.00"), account.getBalance());
        assertEquals(2, account.getOperations().size());
        assertEquals(OperationType.WITHDRAWAL, account.getOperations().get(1).getType());
    }

    @Test
    void withdraw_ShouldThrowException_WhenInsufficientFunds() {
        // Given
        BankAccount account = createTestAccount();
        BigDecimal withdrawAmount = new BigDecimal("100.00");

        // When & Then
        assertThrows(InsufficientFundsException.class,
                () -> accountOperations.withdraw(account.getAccountNum(), withdrawAmount));
    }

    @Test
    void getAccount_ShouldThrowException_WhenAccountNotFound() {
        // Given
        String nonExistentAccountNum = "nonexistent";

        // When & Then
        assertThrows(AccountNotFoundException.class,
                () -> accountOperations.getAccount(nonExistentAccountNum));
    }

    private BankAccount createTestAccount() {
        Client client = Client.builder()
                .lastName("Test Client")
                .build();
        return accountOperations.createAccount(client);
    }
}