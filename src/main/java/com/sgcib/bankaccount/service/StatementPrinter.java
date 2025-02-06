package com.sgcib.bankaccount.service;


import com.sgcib.bankaccount.model.BankAccount;

public interface StatementPrinter {
    String printAccountStatement(BankAccount bankAccount);
}
