package com.sgcib.bankaccount.exception.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ACCOUNT_NOT_FOUND("BANK_001", "Account not found"),
    INVALID_AMOUNT("BANK_002", "Invalid amount"),
    INSUFFICIENT_FUNDS("BANK_003", "Insufficient funds"),
    OPERATION_FAILED("BANK_004", "Operation failed");

    private final String code;
    private final String defaultMessage;
}
