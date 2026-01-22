package com.bank.account.domain;

public class InsufficientFundsException extends AccountException {

    public InsufficientFundsException() {
        super("Insufficient funds");
    }
}
