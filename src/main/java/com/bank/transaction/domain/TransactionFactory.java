package com.bank.transaction.domain;

import com.bank.account.domain.AccountCurrency;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public final class TransactionFactory {

    private TransactionFactory() {
        // no instances
    }

    public static Transaction createTransfer(
            String sourceIban,
            String targetIban,
            BigDecimal amount,
            AccountCurrency currency
    ) {
        return new Transaction(
                UUID.randomUUID(),
                Instant.now(),
                TransactionType.TRANSFER,
                TransactionStatus.PENDING,
                sourceIban,
                targetIban,
                amount,
                currency
        );
    }

    public static Transaction createWithdraw(
            String sourceIban,
            BigDecimal amount,
            String atmId, // zostaje w API
            AccountCurrency currency
    ) {
        return new Transaction(
                UUID.randomUUID(),
                Instant.now(),
                TransactionType.WITHDRAWAL,
                TransactionStatus.COMPLETED,
                sourceIban,
                null,
                amount,
                currency
        );
    }
}
