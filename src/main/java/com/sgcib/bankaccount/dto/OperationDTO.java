package com.sgcib.bankaccount.dto;

import com.sgcib.bankaccount.model.OperationType;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OperationDTO {
    private LocalDateTime date;
    private BigDecimal amount;
    private BigDecimal balance;
    private OperationType type;
}
