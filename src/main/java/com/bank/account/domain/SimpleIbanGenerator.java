package com.bank.account.domain;

import java.security.SecureRandom;

public class SimpleIbanGenerator implements IbanGenerator {

    private static final String COUNTRY_CODE = "SE";
    private static final int ACCOUNT_NUMBER_LENGTH = 16;

    private final SecureRandom random = new SecureRandom();

    @Override
    public String generate() {
        StringBuilder sb = new StringBuilder(COUNTRY_CODE);

        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}
