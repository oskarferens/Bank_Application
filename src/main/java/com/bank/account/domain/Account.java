package com.bank.account.domain;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Account {

    private final String iban;
    private final AccountCurrency currency;
    private BigDecimal balance;
    private BigDecimal dailyLimit;
    private AccountStatus status;

    private final Set<AccountOwner> owners = new HashSet<>();

    private static final int MAX_OWNERS = 2;


    public Account(String iban, AccountOwner primaryOwner) {
        this.iban = iban;
        this.currency = AccountCurrency.SEK;
        this.balance = BigDecimal.ZERO;
        this.dailyLimit = BigDecimal.valueOf(50_000);
        this.status = AccountStatus.ACTIVE;
        this.owners.add(primaryOwner);
    }

    // Technical constructor - reconstruction from the database
    public Account(
            String iban,
            AccountCurrency currency,
            BigDecimal balance,
            BigDecimal dailyLimit,
            AccountStatus status,
            Set<AccountOwner> owners
    ) {
        this.iban = iban;
        this.currency = currency;
        this.balance = balance;
        this.dailyLimit = dailyLimit;
        this.status = status;
        this.owners.addAll(owners);
    }

    // DOMAIN LOGIC
    public void addOwner(AccountOwner owner) {
        if (owners.size() >= MAX_OWNERS) {
            throw new TooManyAccountOwnersException();
        }
        owners.add(owner);
    }

    public void deposit(BigDecimal amount) {
        validateAmount(amount);
        balance = balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        validateAmount(amount);
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        }
        balance = balance.subtract(amount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new AccountException("Amount must be greater than zero");
        }
    }

    //  GETTERS
    public String getIban() {
        return iban;
    }

    public AccountCurrency getCurrency() {
        return currency;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal dailyLimitSnapshot() {
        return dailyLimit;
    }

    public Set<AccountOwner> getOwners() {
        return Set.copyOf(owners);
    }
}
