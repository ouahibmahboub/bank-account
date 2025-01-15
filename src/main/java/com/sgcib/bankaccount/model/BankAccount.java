package com.sgcib.bankaccount.model;

import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
public class BankAccount {
    private BigDecimal balance = BigDecimal.ZERO;
    private List<Operation> operations = new ArrayList<>();
}
