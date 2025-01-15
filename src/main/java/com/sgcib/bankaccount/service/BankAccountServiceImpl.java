package com.sgcib.bankaccount.service;

import com.sgcib.bankaccount.exception.InsufficientFundsException;
import com.sgcib.bankaccount.exception.InvalidOperationException;
import com.sgcib.bankaccount.model.BankAccount;
import com.sgcib.bankaccount.model.Operation;
import com.sgcib.bankaccount.model.OperationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    private final BankAccount account;

    @Override
    public void deposit(BigDecimal amount, LocalDateTime date) {
        validateAmount(amount, "Deposit amount must be positive.");
        BigDecimal newBalance = account.getBalance().add(amount);
        account.getOperations().add(createOperation(amount, newBalance, OperationType.DEPOSIT, date));
        account.setBalance(newBalance);
        log.info("Deposit successful: amount={}, date={}, newBalance={}", amount, date, newBalance);
    }

    @Override
    public void withdraw(BigDecimal amount, LocalDateTime date) {
        validateAmount(amount, "Withdrawal amount must be positive.");
        validateSufficientFunds(amount);
        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.getOperations().add(createOperation(amount.negate(), newBalance, OperationType.WITHDRAWAL, date));
        account.setBalance(newBalance);
        log.info("Withdrawal successful: amount={}, date={}, newBalance={}", amount, date, newBalance);
    }

    @Override
    public List<Operation> getOperations() {
        log.debug("Retrieving operations");
        return account.getOperations();
    }

    @Override
    public BigDecimal getBalance() {
        log.debug("Retrieving balance: {}", account.getBalance());
        return account.getBalance();
    }

    // Helper method to create a operation
    private Operation createOperation(BigDecimal amount, BigDecimal balance, OperationType type, LocalDateTime date) {
        return Operation.builder()
                .amount(amount)
                .balance(balance)
                .type(type)
                .date(date)
                .build();
    }

    // Helper method to validate the amount
    private void validateAmount(BigDecimal amount, String errorMessage) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Invalid amount: {}", amount);
            throw new InvalidOperationException(errorMessage);
        }
    }

    // Helper method to validate sufficient funds
    private void validateSufficientFunds(BigDecimal amount) {
        if (amount.compareTo(account.getBalance()) > 0) {
            log.error("Insufficient funds: amount={}, balance={}", amount, account.getBalance());
            throw new InsufficientFundsException("Insufficient funds.");
        }
    }
}