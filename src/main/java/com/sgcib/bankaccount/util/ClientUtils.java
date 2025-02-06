package com.sgcib.bankaccount.util;


import java.util.UUID;

public class ClientUtils {
    public static String generateClientId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
    }
}
