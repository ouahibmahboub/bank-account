package com.sgcib.bankaccount.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OperationRequest {
    private BigDecimal amount;
}
