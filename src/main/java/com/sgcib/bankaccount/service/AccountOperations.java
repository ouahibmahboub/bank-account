package com.sgcib.bankaccount.service;


import com.sgcib.bankaccount.exception.AccountNotFoundException;
import com.sgcib.bankaccount.exception.InsufficientFundsException;
import com.sgcib.bankaccount.exception.InvalidAmountException;
import com.sgcib.bankaccount.model.BankAccount;
import com.sgcib.bankaccount.model.Client;

import java.math.BigDecimal;

public interface AccountOperations {
    /**
     * Creates a new bank account for a client.
     *
     * @param client the client information
     * @return the created bank account
     */
    BankAccount createAccount(Client client);

    /**
     * Deposits money into an account.
     *
     * @param accountNum the account number
     * @param amount     the amount to deposit
     * @throws AccountNotFoundException if the account is not found
     * @throws InvalidAmountException   if the amount is invalid
     */
    void deposit(String accountNum, BigDecimal amount);

    /**
     * Withdraws money from an account.
     *
     * @param accountNum the account number
     * @param amount     the amount to withdraw
     * @throws AccountNotFoundException   if the account is not found
     * @throws InvalidAmountException     if the amount is invalid
     * @throws InsufficientFundsException if there are insufficient funds
     */
    void withdraw(String accountNum, BigDecimal amount);

    /**
     * Retrieves an account by its number.
     *
     * @param accountNum the account number
     * @return the bank account
     * @throws AccountNotFoundException if the account is not found
     */
    BankAccount getAccount(String accountNum);
}
