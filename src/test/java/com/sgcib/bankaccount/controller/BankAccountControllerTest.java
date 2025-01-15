package com.sgcib.bankaccount.controller;

import com.sgcib.bankaccount.dto.OperationDTO;
import com.sgcib.bankaccount.dto.StatementDTO;
import com.sgcib.bankaccount.exception.InvalidOperationException;
import com.sgcib.bankaccount.mapper.OperationMapper;
import com.sgcib.bankaccount.model.Operation;
import com.sgcib.bankaccount.model.OperationType;
import com.sgcib.bankaccount.service.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BankAccountControllerTest {

    @Mock
    private BankAccountService accountService;

    @Mock
    private OperationMapper operationMapper;

    @InjectMocks
    private BankAccountController bankAccountController;

    private final LocalDateTime testDate = LocalDateTime.now();
    private final String testDateString = testDate.toString();

    @BeforeEach
    void setUp() {
        // Reset mocks before each test
        reset(accountService, operationMapper);
    }

    @Test
    void deposit_ValidAmount_ShouldReturnSuccessMessage() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(100.0);
        doNothing().when(accountService).deposit(amount, testDate);

        // Act
        ResponseEntity<String> response = bankAccountController.deposit(amount, testDateString);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Deposit successful!", response.getBody());
        verify(accountService).deposit(amount, testDate);
    }

    @Test
    void deposit_InvalidAmount_ShouldReturnBadRequest() {
        // Arrange
        BigDecimal invalidAmount = BigDecimal.valueOf(-100.0);
        String errorMessage = "Deposit amount must be positive.";
        doThrow(new InvalidOperationException(errorMessage))
            .when(accountService).deposit(invalidAmount, testDate);

        // Act
        ResponseEntity<String> response = bankAccountController.deposit(invalidAmount, testDateString);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(accountService).deposit(invalidAmount, testDate);
    }

    @Test
    void withdraw_ValidAmount_ShouldReturnSuccessMessage() {
        // Arrange
        BigDecimal amount = BigDecimal.valueOf(50.0);
        doNothing().when(accountService).withdraw(amount, testDate);

        // Act
        ResponseEntity<String> response = bankAccountController.withdraw(amount, testDateString);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Withdrawal successful!", response.getBody());
        verify(accountService).withdraw(amount, testDate);
    }

    @Test
    void withdraw_InvalidAmount_ShouldReturnBadRequest() {
        // Arrange
        BigDecimal invalidAmount = BigDecimal.valueOf(-50.0);
        String errorMessage = "Withdrawal amount must be positive.";
        doThrow(new InvalidOperationException(errorMessage))
            .when(accountService).withdraw(invalidAmount, testDate);

        // Act
        ResponseEntity<String> response = bankAccountController.withdraw(invalidAmount, testDateString);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody());
        verify(accountService).withdraw(invalidAmount, testDate);
    }

    @Test
    void testGetStatement_Success() {
        // Arrange
        Operation depositOperation = Operation.builder()
                .date(testDate)
                .amount(BigDecimal.valueOf(100.0))
                .balance(BigDecimal.valueOf(100.0))
                .type(OperationType.DEPOSIT)
                .build();

        Operation withdrawalOperation = Operation.builder()
                .date(testDate.plusHours(1))
                .amount(BigDecimal.valueOf(-50.0))
                .balance(BigDecimal.valueOf(50.0))
                .type(OperationType.WITHDRAWAL)
                .build();

        List<Operation> operations = List.of(depositOperation, withdrawalOperation);
        when(accountService.getOperations()).thenReturn(operations);

        // Mock the mapper to return DTOs
        when(operationMapper.toOperationDTO(depositOperation)).thenReturn(
                OperationDTO.builder()
                        .date(depositOperation.getDate())
                        .amount(depositOperation.getAmount())
                        .balance(depositOperation.getBalance())
                        .type(depositOperation.getType())
                        .build()
        );

        when(operationMapper.toOperationDTO(withdrawalOperation)).thenReturn(
                OperationDTO.builder()
                        .date(withdrawalOperation.getDate())
                        .amount(withdrawalOperation.getAmount())
                        .balance(withdrawalOperation.getBalance())
                        .type(withdrawalOperation.getType())
                        .build()
        );

        // Act
        ResponseEntity<StatementDTO> response = bankAccountController.getStatement();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getOperations().size());

        // Verify deposit operation
        OperationDTO depositDTO = response.getBody().getOperations().get(0);
        assertEquals(testDate, depositDTO.getDate());
        assertEquals(BigDecimal.valueOf(100.0), depositDTO.getAmount());
        assertEquals(BigDecimal.valueOf(100.0), depositDTO.getBalance());
        assertEquals("DEPOSIT", depositDTO.getType().name());

        // Verify withdrawal operation
        OperationDTO withdrawalDTO = response.getBody().getOperations().get(1);
        assertEquals(testDate.plusHours(1), withdrawalDTO.getDate());
        assertEquals(BigDecimal.valueOf(-50.0), withdrawalDTO.getAmount());
        assertEquals(BigDecimal.valueOf(50.0), withdrawalDTO.getBalance());
        assertEquals("WITHDRAWAL", withdrawalDTO.getType().name());

        verify(accountService).getOperations();
        verify(operationMapper).toOperationDTO(depositOperation);
        verify(operationMapper).toOperationDTO(withdrawalOperation);
    }
}