package com.bank.account.application;

import com.bank.account.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    private AccountRepository accountRepository;
    private IbanGenerator ibanGenerator;
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        accountRepository = mock(AccountRepository.class);
        ibanGenerator = mock(IbanGenerator.class);
        accountService = new AccountServiceImpl(accountRepository, ibanGenerator);
    }

    @Test
    void shouldCreateAccountWithGeneratedIbanAndOwner() {
        // given
        Long userId = 1L;
        String generatedIban = "SEK123456789";

        when(ibanGenerator.generate()).thenReturn(generatedIban);
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Account result = accountService.createAccount(userId);

        // then
        ArgumentCaptor<Account> captor = ArgumentCaptor.forClass(Account.class);
        verify(accountRepository).save(captor.capture());

        Account savedAccount = captor.getValue();

        assertThat(savedAccount.getIban()).isEqualTo(generatedIban);
        assertThat(savedAccount.getOwners()).hasSize(1);
        assertThat(savedAccount.getOwners().iterator().next().getUserId())
                .isEqualTo(userId);
        assertThat(result).isSameAs(savedAccount);
    }

    @Test
    void shouldAddSecondOwnerToAccount() {
        // given
        String iban = "SEK123";
        Account account = new Account(iban, new AccountOwner(1L));

        when(accountRepository.findByIban(iban))
                .thenReturn(java.util.Optional.of(account));
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Account result = accountService.addSecondOwner(iban, 2L);

        // then
        assertThat(result.getOwners()).hasSize(2);
        assertThat(result.getOwners())
                .extracting(AccountOwner::getUserId)
                .containsExactlyInAnyOrder(1L, 2L);

        verify(accountRepository).save(account);
    }

    @Test
    void shouldDepositMoneyToAccount() {
        // given
        String iban = "SEK123";
        Account account = new Account(iban, new AccountOwner(1L));

        when(accountRepository.findByIban(iban))
                .thenReturn(java.util.Optional.of(account));
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Account result = accountService.deposit(iban, new BigDecimal("500.00"));

        // then
        assertThat(result.getBalance()).isEqualByComparingTo("500.00");
        verify(accountRepository).save(account);
    }

    @Test
    void shouldNotAllowDepositOfZeroOrNegativeAmount() {
        // given
        String iban = "SEK123";
        Account account = new Account(iban, new AccountOwner(1L));

        when(accountRepository.findByIban(iban))
                .thenReturn(java.util.Optional.of(account));

        // when / then
        assertThrows(AccountException.class, () ->
                accountService.deposit(iban, BigDecimal.ZERO)
        );
    }

    @Test
    void shouldWithdrawMoneyFromAccount() {
        // given
        String iban = "SEK123";
        Account account = new Account(iban, new AccountOwner(1L));
        account.deposit(new BigDecimal("1000.00"));

        when(accountRepository.findByIban(iban))
                .thenReturn(java.util.Optional.of(account));
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Account result = accountService.withdraw(iban, new BigDecimal("400.00"));

        // then
        assertThat(result.getBalance()).isEqualByComparingTo("600.00");
        verify(accountRepository).save(account);
    }

    @Test
    void shouldNotAllowWithdrawMoreThanBalance() {
        // given
        String iban = "SEK123";
        Account account = new Account(iban, new AccountOwner(1L));
        account.deposit(new BigDecimal("300.00"));

        when(accountRepository.findByIban(iban))
                .thenReturn(java.util.Optional.of(account));

        // when / then
        assertThrows(InsufficientFundsException.class, () ->
                accountService.withdraw(iban, new BigDecimal("500.00"))
        );
    }

    @Test
    void shouldReturnAccountForUserIfUserIsOwner() {
        // given
        String iban = "SEK123";
        Long userId = 1L;

        Account account = new Account(iban, new AccountOwner(userId));

        when(accountRepository.findByIban(iban))
                .thenReturn(java.util.Optional.of(account));

        // when
        Account result = accountService.getAccountByIbanForUser(iban, userId);

        // then
        assertThat(result).isSameAs(account);
    }

    @Test
    void shouldThrowExceptionIfUserIsNotOwner() {
        // given
        String iban = "SEK123";
        Long ownerId = 1L;
        Long otherUserId = 2L;

        Account account = new Account(iban, new AccountOwner(ownerId));

        when(accountRepository.findByIban(iban))
                .thenReturn(java.util.Optional.of(account));

        // when / then
        assertThrows(AccountException.class, () ->
                accountService.getAccountByIbanForUser(iban, otherUserId)
        );
    }

    @Test
    void shouldReturnAccountsForUser() {
        // given
        Long userId = 1L;

        Account a1 = new Account("SEK1", new AccountOwner(userId));
        Account a2 = new Account("SEK2", new AccountOwner(userId));

        when(accountRepository.findByUserId(userId))
                .thenReturn(List.of(a1, a2));

        // when
        List<Account> result = accountService.getAccountsForUser(userId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(Account::getIban)
                .containsExactlyInAnyOrder("SEK1", "SEK2");
    }


}
