package com.bank.transaction.application;

import com.bank.transaction.domain.Transaction;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface TransactionService {

    Transaction transferBetweenAccounts(
            String sourceIban,
            String targetIban,
            BigDecimal amount
    );

    Transaction withdrawFromAccount(
            String sourceIban,
            BigDecimal amount,
            String atmId
    );

    List<Transaction> getTransactionsForAccount(String iban);

    // ONLY DECLARATION HERE
    Transaction getTransactionById(UUID id);
}