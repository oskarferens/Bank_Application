package com.bank.account.application;

import com.bank.account.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final IbanGenerator ibanGenerator;

    public AccountServiceImpl(
            AccountRepository accountRepository,
            IbanGenerator ibanGenerator
    ) {
        this.accountRepository = accountRepository;
        this.ibanGenerator = ibanGenerator;
    }

    @Override
    public Account createAccount(Long ownerUserId) {
        String iban = ibanGenerator.generate();

        Account account = new Account(
                iban,
                new AccountOwner(ownerUserId)
        );

        return accountRepository.save(account);
    }

    @Override
    public Account addSecondOwner(String iban, Long secondOwnerUserId) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new IllegalStateException("Account not found"));

        account.addOwner(new AccountOwner(secondOwnerUserId));

        return accountRepository.save(account);
    }

    @Override
    public Account deposit(String iban, BigDecimal amount) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new IllegalStateException("Account not found"));

        account.deposit(amount);

        return accountRepository.save(account);
    }

    @Override
    public Account withdraw(String iban, BigDecimal amount) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountException("Account not found"));

        account.withdraw(amount);

        return accountRepository.save(account);
    }


    @Override
    public List<Account> getAccountsForUser(Long userId) {
        return accountRepository.findByUserId(userId);
    }


    @Override
    public Account getAccountByIbanForUser(String iban, Long userId) {
        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountException("Account not found"));

        boolean isOwner = account.getOwners().stream()
                .anyMatch(owner -> owner.getUserId().equals(userId));

        if (!isOwner) {
            throw new AccountException("User is not owner of this account");
        }
        return account;
    }
}
