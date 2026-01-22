package com.bank.account.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;
    private AccountOwner owner;

    @BeforeEach
    void setUp() {
        owner = new AccountOwner(1L);
        account = new Account("SE1234567890", owner);
    }

    @Test
    void shouldDepositMoney() {
        account.deposit(BigDecimal.valueOf(100));

        assertEquals(BigDecimal.valueOf(100), account.getBalance());
    }

    @Test
    void shouldNotAllowWithdrawMoreThanBalance() {
        AccountException exception = assertThrows(
                AccountException.class,
                () -> account.withdraw(BigDecimal.valueOf(50))
        );

        assertEquals("Insufficient funds", exception.getMessage());
    }

    @Test
    void shouldAllowMaximumTwoOwners() {
        account.addOwner(new AccountOwner(2L));

        assertEquals(2, account.getOwners().size());
    }

    @Test
    void shouldNotAllowMoreThanTwoOwners() {
        account.addOwner(new AccountOwner(2L));

        AccountException exception = assertThrows(
                AccountException.class,
                () -> account.addOwner(new AccountOwner(3L))
        );

        assertEquals(
                "Account cannot have more than 2 owners",
                exception.getMessage()
        );
    }

    @Test
    void shouldNotAllowNegativeAmount() {
        AccountException exception = assertThrows(
                AccountException.class,
                () -> account.deposit(BigDecimal.valueOf(-10))
        );

        assertEquals("Amount must be greater than zero", exception.getMessage());
    }
}

