package com.bank.account.api;

import com.bank.account.application.AccountService;
import com.bank.account.domain.Account;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // CREATE ACCOUNT
    @PostMapping
    public Account createAccount(@RequestParam Long ownerUserId) {
        return accountService.createAccount(ownerUserId);
    }

    // GET ACCOUNT BY IBAN
    @GetMapping("/{iban}")
    public Account getAccount(
            @PathVariable String iban,
            @RequestParam Long userId
    ) {
        return accountService.getAccountByIbanForUser(iban, userId);
    }

    // GET ALL ACCOUNTS FOR USER
    @GetMapping
    public List<Account> getAccountsForUser(@RequestParam Long userId) {
        return accountService.getAccountsForUser(userId);
    }
}
