package com.sgcib.bankaccount.service;

import com.sgcib.bankaccount.exception.InsufficientFundsException;
import com.sgcib.bankaccount.exception.InvalidOperationException;
import com.sgcib.bankaccount.model.BankAccount;
import com.sgcib.bankaccount.model.Operation;
import com.sgcib.bankaccount.model.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {

    @Mock
    private BankAccount account;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    private final LocalDateTime testDate = LocalDateTime.now();

    @BeforeEach
    void setUp() {
        // Initialize the account with a balance of 1000 and an empty list of operations
        lenient().when(account.getBalance()).thenReturn(BigDecimal.valueOf(1000.0));
        lenient().when(account.getOperations()).thenReturn(new ArrayList<>());
    }

    @Test
    void testDeposit_Success() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(500.0);
        BigDecimal expectedBalance = BigDecimal.valueOf(1500.0);

        // Act
        bankAccountService.deposit(amount, testDate);

        // Assert
        verify(account).setBalance(expectedBalance); // Verify setBalance is called with the correct value
        verify(account).getOperations(); // Verify getOperations is called
    }

    @Test
    void testDeposit_InvalidAmount() {
        // Arrange
        BigDecimal invalidAmount = BigDecimal.valueOf(-100.0);
        String errorMessage = "Deposit amount must be positive.";

        // Act & Assert
        InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> {
            bankAccountService.deposit(invalidAmount, testDate);
        });
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testWithdraw_Success() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(200.0);
        BigDecimal expectedBalance = BigDecimal.valueOf(800.0);

        // Act
        bankAccountService.withdraw(amount, testDate);

        // Assert
        verify(account).setBalance(expectedBalance); // Verify setBalance is called with the correct value
        verify(account).getOperations(); // Verify getOperations is called
    }

    @Test
    void testWithdraw_InvalidAmount() {
        // Arrange
        BigDecimal invalidAmount = BigDecimal.valueOf(-100.0);
        String errorMessage = "Withdrawal amount must be positive.";

        // Act & Assert
        InvalidOperationException exception = assertThrows(InvalidOperationException.class, () -> {
            bankAccountService.withdraw(invalidAmount, testDate);
        });
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testWithdraw_InsufficientFunds() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(1500.0);
        String errorMessage = "Insufficient funds.";

        // Act & Assert
        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () -> {
            bankAccountService.withdraw(amount, testDate);
        });
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testGetOperations() {
        // Arrange
        List<Operation> operations = new ArrayList<>();
        operations.add(Operation.builder()
                .date(testDate)
                .amount(BigDecimal.valueOf(100.0))
                .balance(BigDecimal.valueOf(1100.0))
                .type(OperationType.DEPOSIT)
                .build());
        when(account.getOperations()).thenReturn(operations);

        // Act
        List<Operation> result = bankAccountService.getOperations();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(BigDecimal.valueOf(100.0), result.get(0).getAmount());
    }

    @Test
    void testGetBalance() {
        // Arrange
        when(account.getBalance()).thenReturn(BigDecimal.valueOf(1000.0));

        // Act
        BigDecimal balance = bankAccountService.getBalance();

        // Assert
        assertEquals(BigDecimal.valueOf(1000.0), balance);
    }
}