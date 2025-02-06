package com.sgcib.bankaccount.exception;

import com.sgcib.bankaccount.exception.error.ErrorCode;

public class AccountNotFoundException extends BankException {
    public AccountNotFoundException(String accountNum) {
        super(ErrorCode.ACCOUNT_NOT_FOUND,
                String.format("Account not found with number: %s", accountNum));
    }
}
