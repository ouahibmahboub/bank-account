package com.sgcib.bankaccount.service.impl;

import com.sgcib.bankaccount.exception.AccountNotFoundException;
import com.sgcib.bankaccount.model.BankAccount;
import com.sgcib.bankaccount.model.Operation;
import com.sgcib.bankaccount.model.OperationType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatementPrinterImplTest {

    @Mock
    private BankAccount bankAccount;

    @Mock
    private List<Operation> operations;

    @InjectMocks
    private StatementPrinterImpl statementPrinter;

    @Test
    void printStatement_ShouldIncludeAllOperationsFormatted() {
        // Given
        Operation deposit = Operation.builder()
                .type(OperationType.DEPOSIT)
                .amount(new BigDecimal("100.00"))
                .balance(new BigDecimal("100.00"))
                .operationDate(LocalDateTime.now())
                .build();

        when(bankAccount.getOperations()).thenReturn(operations);
        when(operations.stream()).thenReturn(Stream.of(deposit));

        // When
        String statement = statementPrinter.printAccountStatement(bankAccount);

        // Then
        assertTrue(statement.contains("DEPOSIT"));
        assertTrue(statement.contains("+100.00"));
    }

    @Test
    void printStatement_ShouldThrowAccountNotFoundException_WhenAccountIsNull() {
        // When/Then
        assertThrows(AccountNotFoundException.class,
                () -> statementPrinter.printAccountStatement(null));
    }
}
