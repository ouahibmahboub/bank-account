package com.sgcib.bankaccount.exception;

import com.sgcib.bankaccount.exception.error.ErrorCode;

public class InvalidAmountException extends BankException {
    public InvalidAmountException(String message) {
        super(ErrorCode.INVALID_AMOUNT, message);
    }
}