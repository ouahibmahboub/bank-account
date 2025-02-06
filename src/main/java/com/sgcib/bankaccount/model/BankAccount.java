package com.sgcib.bankaccount.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    private String accountNum;
    private Client client;
    @Builder.Default
    private LocalDate creationDate = LocalDate.now();
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;
    @Builder.Default
    private List<Operation> operations = new ArrayList<>();
}