package com.bank.transaction.domain;

import com.bank.account.domain.AccountCurrency;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
public class Transaction {

    @Id
    private UUID id;

    private Instant createdAt;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String sourceAccountIban;
    private String targetAccountIban;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private AccountCurrency currency;

    // NEEDED FOR JPA
    protected Transaction() {
    }

    public Transaction(
            UUID id,
            Instant createdAt,
            TransactionType type,
            TransactionStatus status,
            String sourceAccountIban,
            String targetAccountIban,
            BigDecimal amount,
            AccountCurrency currency
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.type = type;
        this.status = status;
        this.sourceAccountIban = sourceAccountIban;
        this.targetAccountIban = targetAccountIban;
        this.amount = amount;
        this.currency = currency;
    }
}