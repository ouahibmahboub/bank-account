package com.sgcib.bankaccount.model;

import com.sgcib.bankaccount.util.ClientUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Client {
    private String id = ClientUtils.generateClientId();
    private String firstName;
    private String lastName;
    private String address;
}
