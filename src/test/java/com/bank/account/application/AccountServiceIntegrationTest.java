package com.bank.account.application;

import com.bank.account.domain.Account;
import com.bank.account.domain.AccountOwner;
import com.bank.account.domain.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void shouldCreateAccountAndPersistIt() {
        // when
        Account account = accountService.createAccount(1L);

        // then
        assertThat(account.getIban()).isNotNull();
        assertThat(account.getOwners()).hasSize(1);

        Account persisted = accountRepository
                .findByIban(account.getIban())
                .orElseThrow();

        assertThat(persisted.getIban()).isEqualTo(account.getIban());
        assertThat(persisted.getOwners()).hasSize(1);
    }

    @Test
    void shouldAddSecondOwnerAndPersistChange() {
        // given
        Account account = accountService.createAccount(1L);

        // when
        Account updated = accountService.addSecondOwner(
                account.getIban(),
                2L
        );

        // then
        assertThat(updated.getOwners()).hasSize(2);

        Account persisted = accountRepository
                .findByIban(account.getIban())
                .orElseThrow();

        assertThat(persisted.getOwners())
                .extracting(AccountOwner::getUserId)
                .containsExactlyInAnyOrder(1L, 2L);
    }
    @Test
    void shouldReturnAccountsForUser() {
        // given
        Account a1 = accountService.createAccount(1L);
        Account a2 = accountService.createAccount(1L);
        accountService.createAccount(2L); // inny user

        // when
        var accounts = accountService.getAccountsForUser(1L);

        // then
        assertThat(accounts).hasSize(2);
        assertThat(accounts)
                .extracting(Account::getIban)
                .containsExactlyInAnyOrder(
                        a1.getIban(),
                        a2.getIban()
                );
    }
}
