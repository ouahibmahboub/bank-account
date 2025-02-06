package com.sgcib.bankaccount.controller;

import com.sgcib.bankaccount.dto.OperationRequest;
import com.sgcib.bankaccount.model.BankAccount;
import com.sgcib.bankaccount.model.Client;
import com.sgcib.bankaccount.service.AccountOperations;
import com.sgcib.bankaccount.service.StatementPrinter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class BankAccountController {
    private final AccountOperations accountOperations;
    private final StatementPrinter statementPrinter;

    @PostMapping
    public ResponseEntity<BankAccount> createAccount(@RequestBody Client client) {
        return ResponseEntity.ok(accountOperations.createAccount(client));
    }

    @PostMapping("/{accountNum}/deposit")
    public ResponseEntity<Map<String, BigDecimal>> deposit(
            @PathVariable String accountNum,
            @RequestBody OperationRequest request) {
        accountOperations.deposit(accountNum, request.getAmount());
        BankAccount account = accountOperations.getAccount(accountNum);
        return ResponseEntity.ok(Map.of("balance", account.getBalance()));
    }

    @PostMapping("/{accountNum}/withdraw")
    public ResponseEntity<Map<String, BigDecimal>> withdraw(
            @PathVariable String accountNum,
            @RequestBody OperationRequest request) {
        accountOperations.withdraw(accountNum, request.getAmount());
        BankAccount account = accountOperations.getAccount(accountNum);
        return ResponseEntity.ok(Map.of("balance", account.getBalance()));
    }

    @GetMapping("/{accountNum}/statement")
    public ResponseEntity<String> getStatement(@PathVariable String accountNum) {
        BankAccount account = accountOperations.getAccount(accountNum);
        String statement = statementPrinter.printAccountStatement(account);
        return ResponseEntity.ok(statement);
    }
}