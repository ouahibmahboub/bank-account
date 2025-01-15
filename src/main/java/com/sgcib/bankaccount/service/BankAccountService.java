package com.sgcib.bankaccount.service;

import com.sgcib.bankaccount.model.Operation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


public interface BankAccountService {
    void deposit(BigDecimal amount, LocalDateTime date);
    void withdraw(BigDecimal amount, LocalDateTime date);
    List<Operation> getOperations();
    BigDecimal getBalance();
}
