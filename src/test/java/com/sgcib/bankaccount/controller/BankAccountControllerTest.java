package com.sgcib.bankaccount.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sgcib.bankaccount.dto.OperationRequest;
import com.sgcib.bankaccount.exception.InsufficientFundsException;
import com.sgcib.bankaccount.exception.InvalidAmountException;
import com.sgcib.bankaccount.exception.error.GlobalExceptionHandler;
import com.sgcib.bankaccount.model.BankAccount;
import com.sgcib.bankaccount.model.Client;
import com.sgcib.bankaccount.service.AccountOperations;
import com.sgcib.bankaccount.service.StatementPrinter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BankAccountControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private AccountOperations accountService;

    @Mock
    private StatementPrinter statementPrinter;

    @InjectMocks
    private BankAccountController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void createAccount_ShouldReturnCreatedAccount() throws Exception {
        // Given
        Client client = Client.builder()
                .lastName("Smith")
                .build();

        BankAccount expectedAccount = BankAccount.builder()
                .accountNum("12345678")
                .client(client)
                .balance(BigDecimal.ZERO)
                .operations(new ArrayList<>())
                .build();

        when(accountService.createAccount(any(Client.class))).thenReturn(expectedAccount);

        // When & Then
        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(client)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNum").value("12345678"))
                .andExpect(jsonPath("$.balance").value("0"))
                .andExpect(jsonPath("$.client.lastName").value("Smith"));
    }

    @Test
    void deposit_ShouldReturnUpdatedBalance() throws Exception {
        // Given
        String accountNum = "12345678";
        BigDecimal amount = new BigDecimal("100.00");
        OperationRequest request = new OperationRequest(amount);

        BankAccount updatedAccount = BankAccount.builder()
                .accountNum(accountNum)
                .balance(amount)
                .build();

        when(accountService.getAccount(accountNum)).thenReturn(updatedAccount);

        // When & Then
        mockMvc.perform(post("/api/accounts/{accountNum}/deposit", accountNum)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("100.0"));

        verify(accountService).deposit(accountNum, amount);
    }

    @Test
    void withdraw_ShouldReturnUpdatedBalance() throws Exception {
        // Given
        String accountNum = "12345678";
        BigDecimal initialBalance = new BigDecimal("100.00");
        BigDecimal withdrawAmount = new BigDecimal("50.00");
        OperationRequest request = new OperationRequest(withdrawAmount);

        BankAccount updatedAccount = BankAccount.builder()
                .accountNum(accountNum)
                .balance(initialBalance.subtract(withdrawAmount))
                .build();

        when(accountService.getAccount(accountNum)).thenReturn(updatedAccount);

        // When & Then
        mockMvc.perform(post("/api/accounts/{accountNum}/withdraw", accountNum)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value("50.0"));

        verify(accountService).withdraw(accountNum, withdrawAmount);
    }

    @Test
    void getStatement_ShouldReturnAccountStatement() throws Exception {
        // Given
        String accountNum = "12345678";
        BankAccount account = BankAccount.builder()
                .accountNum(accountNum)
                .balance(BigDecimal.valueOf(100))
                .operations(new ArrayList<>())
                .build();

        String expectedStatement = "Account Statement";

        when(accountService.getAccount(accountNum)).thenReturn(account);
        when(statementPrinter.printAccountStatement(account)).thenReturn(expectedStatement);

        // When & Then
        mockMvc.perform(get("/api/accounts/{accountNum}/statement", accountNum))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedStatement));
    }

    @Test
    void deposit_ShouldReturnBadRequest_WhenAmountIsInvalid() throws Exception {
        // Given
        String accountNum = "12345678";
        BigDecimal invalidAmount = new BigDecimal("-100.00");
        OperationRequest request = new OperationRequest(invalidAmount);
doThrow(new InvalidAmountException("Amount must be positive")).when(accountService).deposit(accountNum, invalidAmount);

        // When & Then
        mockMvc.perform(post("/api/accounts/{accountNum}/deposit", accountNum)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Amount must be positive"));
    }

    @Test
    void withdraw_ShouldReturnBadRequest_WhenInsufficientFunds() throws Exception {
        // Given
        String accountNum = "12345678";
        BigDecimal amount = new BigDecimal("1000.00");
        OperationRequest request = new OperationRequest(amount);
doThrow(new InsufficientFundsException(accountNum, amount)).when(accountService).withdraw(accountNum, amount);


        // When & Then
        mockMvc.perform(post("/api/accounts/{accountNum}/withdraw", accountNum)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}