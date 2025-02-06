package com.sgcib.bankaccount.exception;

import com.sgcib.bankaccount.exception.error.ErrorCode;

import java.math.BigDecimal;

public class InsufficientFundsException extends BankException {
    public InsufficientFundsException(String accountNum, BigDecimal amount) {
        super(ErrorCode.INSUFFICIENT_FUNDS,
                String.format("Insufficient funds for account %s to withdraw %s", accountNum, amount));
    }
}