package com.sgcib.bankaccount.exception;

import com.sgcib.bankaccount.exception.error.ErrorCode;

public abstract class BankException extends RuntimeException {
    private final ErrorCode errorCode;

    protected BankException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
