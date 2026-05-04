package com.bank.transaction.api;

import com.bank.transaction.application.TransactionService;
import com.bank.transaction.domain.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    // TRANSFER
    @PostMapping("/transfer")
    public Transaction transfer(
            @RequestParam(required = false) String sourceIban,
            @RequestParam String targetIban,
            @RequestParam BigDecimal amount
    ) {
        return transactionService.transferBetweenAccounts(
                sourceIban,
                targetIban,
                amount
        );
    }

    // WITHDRAW
    @PostMapping("/withdraw")
    public Transaction withdraw(
            @RequestParam String sourceIban,
            @RequestParam BigDecimal amount,
            @RequestParam String atmId
    ) {
        return transactionService.withdrawFromAccount(
                sourceIban,
                amount,
                atmId
        );
    }

    // HISTORY
    @GetMapping
    public List<Transaction> getTransactions(
            @RequestParam String iban
    ) {
        return transactionService.getTransactionsForAccount(iban);
    }

    // SINGLE
    @GetMapping("/{id}")
    public Transaction getById(@PathVariable UUID id) {
        return transactionService.getTransactionById(id);
    }
}