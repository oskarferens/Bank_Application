package com.bank.account.application;

import com.bank.account.domain.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    Account createAccount(Long ownerUserId);

    Account addSecondOwner(String iban, Long secondOwnerUserId);

    Account deposit(String iban, BigDecimal amount);

    Account withdraw(String iban, BigDecimal amount);

    List<Account> getAccountsForUser(Long userId);

    Account getAccountByIbanForUser(String iban, Long userId);
}
