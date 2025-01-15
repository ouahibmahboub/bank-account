package com.sgcib.bankaccount.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class Operation {
    private BigDecimal amount;
    private BigDecimal balance;
    private OperationType type;

    @Builder.Default
    LocalDateTime date = LocalDateTime.now();
}
