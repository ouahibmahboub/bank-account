package com.sgcib.bankaccount.config;

import com.sgcib.bankaccount.model.BankAccount;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BankAccountConfig {

    @Bean
    public BankAccount bankAccount() {
        return new BankAccount();
    }
}
