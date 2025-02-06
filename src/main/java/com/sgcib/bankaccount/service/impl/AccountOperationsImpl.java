package com.sgcib.bankaccount.service.impl;

import com.sgcib.bankaccount.exception.AccountNotFoundException;
import com.sgcib.bankaccount.exception.InsufficientFundsException;
import com.sgcib.bankaccount.exception.InvalidAmountException;
import com.sgcib.bankaccount.model.BankAccount;
import com.sgcib.bankaccount.model.Client;
import com.sgcib.bankaccount.model.Operation;
import com.sgcib.bankaccount.model.OperationType;
import com.sgcib.bankaccount.service.AccountOperations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountOperationsImpl implements AccountOperations {
    private final Map<String, BankAccount> accountsStore = new ConcurrentHashMap<>();

    @Override
    public BankAccount createAccount(Client client) {
        log.info("Creating new account for client: {}", client.getId());
        validateClient(client);

        BankAccount account = BankAccount.builder()
                .client(client)
                .balance(BigDecimal.ZERO)
                .operations(new ArrayList<>())
                .accountNum(generateAccountNumber())
                .build();

        accountsStore.put(account.getAccountNum(), account);
        log.info("Account created successfully with number: {}", account.getAccountNum());
        return account;
    }

    @Override
    public void deposit(String accountNum, BigDecimal amount) {
        log.info("Processing deposit of {} for account {}", amount, accountNum);
        BankAccount account = findAccount(accountNum);
        validateAmount(amount);

        BigDecimal newBalance = account.getBalance().add(amount);
        updateAccountBalance(account, amount, newBalance, OperationType.DEPOSIT);
        log.info("Deposit successful. New balance: {}", newBalance);
    }

    @Override
    public void withdraw(String accountNum, BigDecimal amount) {
        log.info("Processing withdrawal of {} for account {}", amount, accountNum);
        BankAccount account = findAccount(accountNum);
        validateAmount(amount);
        validateSufficientFunds(account, amount);

        BigDecimal newBalance = account.getBalance().subtract(amount);
        updateAccountBalance(account, amount, newBalance, OperationType.WITHDRAWAL);
        log.info("Withdrawal successful. New balance: {}", newBalance);
    }

    @Override
    public BankAccount getAccount(String accountNum) {
        return findAccount(accountNum);
    }

    private void updateAccountBalance(BankAccount account, BigDecimal amount,
                                      BigDecimal newBalance, OperationType operationType) {
        Operation operation = Operation.builder()
                .operationDate(LocalDateTime.now())
                .type(operationType)
                .amount(amount)
                .balance(newBalance)
                .build();

        account.setBalance(newBalance);
        account.getOperations().add(operation);
    }

    private void validateClient(Client client) {
        if (client == null) {
            log.error("Client information is null");
            throw new IllegalArgumentException("Client information cannot be null");
        }
    }

    private BankAccount findAccount(String accountNum) {
        return Optional.ofNullable(accountsStore.get(accountNum))
                .orElseThrow(() -> {
                    log.error("Account not found: {}", accountNum);
                    return new AccountNotFoundException(accountNum);
                });
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            log.error("Invalid amount: {}", amount);
            throw new InvalidAmountException("Amount must be positive");
        }
    }

    private void validateSufficientFunds(BankAccount account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            String message = String.format("Insufficient funds for withdrawal. Account: %s, Required: %s, Available: %s",
                    account.getAccountNum(), amount, account.getBalance());
            log.error(message);
            throw new InsufficientFundsException(account.getAccountNum(), amount);
        }
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString().substring(0, 10).toUpperCase();
    }
}