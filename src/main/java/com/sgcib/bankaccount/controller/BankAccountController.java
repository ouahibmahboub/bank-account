package com.sgcib.bankaccount.controller;

import com.sgcib.bankaccount.dto.StatementDTO;
import com.sgcib.bankaccount.exception.InvalidOperationException;
import com.sgcib.bankaccount.mapper.OperationMapper;
import com.sgcib.bankaccount.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Bank Account API", description = "API for managing bank account operations")
public class BankAccountController {
    private final BankAccountService accountService;
    private final OperationMapper operationMapper;

    @PostMapping("/deposit")
    @Operation(
        summary = "Deposit money into the account",
        description = "Deposits the specified amount into the account on the given date."
    )
    @ApiResponse(responseCode = "200", description = "Deposit successful")
    @ApiResponse(responseCode = "400", description = "Invalid deposit amount")
    public ResponseEntity<String> deposit(
        @Parameter(description = "Amount to deposit", example = "100.0", required = true)
        @RequestParam BigDecimal amount,

        @Parameter(description = "Date of the deposit (yyyy-MM-dd'T'HH:mm:ss)", example = "2023-10-01T12:00:00", required = true)
        @RequestParam String date
    ) {
        try {
            LocalDateTime operationDate = LocalDateTime.parse(date);
            accountService.deposit(amount, operationDate);
            log.info("Deposit request processed: amount={}, date={}", amount, date);
            return ResponseEntity.ok("Deposit successful!");
        } catch (InvalidOperationException e) {
            log.error("Invalid deposit: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/withdraw")
    @Operation(
        summary = "Withdraw money from the account",
        description = "Withdraws the specified amount from the account on the given date."
    )
    @ApiResponse(responseCode = "200", description = "Withdrawal successful")
    @ApiResponse(responseCode = "400", description = "Invalid withdrawal amount or insufficient funds")
    public ResponseEntity<String> withdraw(
        @Parameter(description = "Amount to withdraw", example = "50.0", required = true)
        @RequestParam BigDecimal amount,

        @Parameter(description = "Date of the withdrawal (yyyy-MM-dd'T'HH:mm:ss)", example = "2023-10-01T12:00:00", required = true)
        @RequestParam String date
    ) {
        try {
            LocalDateTime operationDate = LocalDateTime.parse(date);
            accountService.withdraw(amount, operationDate);
            log.info("Withdrawal request processed: amount={}, date={}", amount, date);
            return ResponseEntity.ok("Withdrawal successful!");
        } catch (InvalidOperationException e) {
            log.error("Invalid withdrawal: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/statement")
    @Operation(
        summary = "Get account statement",
        description = "Retrieves the account statement, including all transactions."
    )
    @ApiResponse(responseCode = "200", description = "Account statement retrieved successfully")
    public ResponseEntity<StatementDTO> getStatement() {
        log.debug("Generating account statement");
        StatementDTO statementDTO = StatementDTO.builder()
            .operations(
                accountService.getOperations().stream()
                    .map(operationMapper::toOperationDTO)
                    .collect(Collectors.toList())
            )
            .build();
        return ResponseEntity.ok(statementDTO);
    }
}