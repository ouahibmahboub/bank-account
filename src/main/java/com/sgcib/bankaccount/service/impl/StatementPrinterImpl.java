package com.sgcib.bankaccount.service.impl;


import com.sgcib.bankaccount.exception.AccountNotFoundException;
import com.sgcib.bankaccount.model.BankAccount;
import com.sgcib.bankaccount.model.Operation;
import com.sgcib.bankaccount.model.OperationType;
import com.sgcib.bankaccount.service.StatementPrinter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StatementPrinterImpl implements StatementPrinter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final String SEPARATOR = "|";
    private static final String NEW_LINE = "\n";
    private static final String DASHED_LINE = "-".repeat(100) + NEW_LINE;
    private static final List<String> HEADER_COLUMNS = Arrays.asList(
            "Date",
            "Operation",
            "Amount",
            "Balance"
    );

    @Override
    public String printAccountStatement(BankAccount bankAccount) {
        validateBankAccount(bankAccount);

        StringBuilder statement = new StringBuilder()
                .append(DASHED_LINE)
                .append(formatHeader())
                .append(formatOperations(bankAccount))
                .append(DASHED_LINE);

        log.info("Generated account statement for account: {}", bankAccount.getAccountNum());

        return statement.toString();
    }

    private void validateBankAccount(BankAccount bankAccount) {
        if (bankAccount == null) {
            log.error("Attempted to print statement for null bank account");
            throw new AccountNotFoundException("Unknown");
        }
    }

    private String formatHeader() {
        return HEADER_COLUMNS.stream()
                .collect(Collectors.joining(SEPARATOR, "", NEW_LINE));
    }

    private String formatOperations(BankAccount bankAccount) {
        return bankAccount.getOperations().stream()
                .sorted(Comparator.comparing(Operation::getOperationDate).reversed())
                .map(this::formatOperation)
                .collect(Collectors.joining());
    }

    private String formatOperation(Operation operation) {
        return String.format("%s%s%s%s%s%s%s%s%s",
                operation.getOperationDate().format(DATE_FORMATTER),
                SEPARATOR,
                operation.getType(),
                SEPARATOR,
                formatAmount(operation),
                SEPARATOR,
                operation.getBalance(),
                SEPARATOR,
                NEW_LINE
        );
    }

    private String formatAmount(Operation operation) {
        return operation.getType() == OperationType.WITHDRAWAL
                ? "-" + operation.getAmount()
                : "+" + operation.getAmount();
    }
}