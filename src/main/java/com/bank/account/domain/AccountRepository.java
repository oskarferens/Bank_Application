package com.bank.account.domain;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);
    Optional<Account> findByIban(String iban);
    List<Account> findByUserId(Long userId);
}
