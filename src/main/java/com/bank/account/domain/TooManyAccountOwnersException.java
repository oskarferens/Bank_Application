package com.bank.account.domain;

public class TooManyAccountOwnersException extends AccountException {

    public TooManyAccountOwnersException() {
        super("Account cannot have more than 2 owners");
    }
}
