package com.bank.transaction.application;

import com.bank.account.domain.Account;
import com.bank.account.domain.AccountRepository;
import com.bank.transaction.domain.Transaction;
import com.bank.transaction.domain.TransactionFactory;
import com.bank.transaction.domain.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository
    ) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Transaction transferBetweenAccounts(
            String sourceIban,
            String targetIban,
            BigDecimal amount
    ) {

        Account sourceAccount = null;

        // 🔹 jeśli jest source → normalny transfer
        if (sourceIban != null) {
            sourceAccount = accountRepository.findByIban(sourceIban)
                    .orElseThrow(() -> new IllegalStateException("Source account not found"));

            sourceAccount.withdraw(amount);
            accountRepository.save(sourceAccount);
        }

        // 🔹 target ZAWSZE musi być
        Account targetAccount = accountRepository.findByIban(targetIban)
                .orElseThrow(() -> new IllegalStateException("Target account not found"));

        targetAccount.deposit(amount);
        accountRepository.save(targetAccount);

        // 🔹 Transaction
        Transaction transaction = TransactionFactory.createTransfer(
                sourceIban,
                targetIban,
                amount,
                targetAccount.getCurrency()
        );

        transactionRepository.save(transaction);

        return transaction;
    }

    @Override
    public Transaction withdrawFromAccount(
            String sourceIban,
            BigDecimal amount,
            String atmId
    ) {

        Account account = accountRepository.findByIban(sourceIban)
                .orElseThrow(() -> new IllegalStateException("Account not found"));

        account.withdraw(amount);

        accountRepository.save(account);

        Transaction transaction = TransactionFactory.createWithdraw(
                sourceIban,
                amount,
                atmId,
                account.getCurrency()
        );
        transactionRepository.save(transaction);

        return transaction;
    }

    @Override
    public List<Transaction> getTransactionsForAccount(String iban) {
        return transactionRepository
                .findBySourceAccountIbanOrTargetAccountIban(iban, iban);
    }

    @Override
    public Transaction getTransactionById(UUID id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Transaction not found"));
    }
}