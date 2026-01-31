package com.bank.account.infrastructure.persistence;

import com.bank.account.domain.Account;
import com.bank.account.domain.AccountRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class JpaAccountRepository implements AccountRepository {

    private final SpringDataAccountJpaRepository jpaRepository;

    public JpaAccountRepository(SpringDataAccountJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Account save(Account account) {

        AccountEntity entity = jpaRepository
                .findByIban(account.getIban())
                .map(existing -> AccountMapper.updateEntity(existing, account))
                .orElseGet(() -> AccountMapper.toEntity(account));

        AccountEntity saved = jpaRepository.save(entity);
        return AccountMapper.toDomain(saved);
    }

    @Override
    public Optional<Account> findByIban(String iban) {
        return jpaRepository.findByIban(iban)
                .map(AccountMapper::toDomain);
    }

    @Override
    public List<Account> findByUserId(Long userId) {
        return jpaRepository.findAllByUserId(userId)
                .stream()
                .map(AccountMapper::toDomain)
                .toList();
    }
}
