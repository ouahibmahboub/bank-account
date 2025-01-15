package com.sgcib.bankaccount.exception;


public class InsufficientFundsException extends InvalidOperationException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
