package com.sgcib.bankaccount.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Operation {
    private LocalDateTime operationDate;
    private OperationType type;
    private BigDecimal amount;
    private BigDecimal balance;
}
