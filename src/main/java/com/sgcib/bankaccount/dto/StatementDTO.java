package com.sgcib.bankaccount.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class StatementDTO {
    private List<OperationDTO> operations;
}
