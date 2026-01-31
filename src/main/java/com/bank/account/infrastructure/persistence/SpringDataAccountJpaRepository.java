package com.bank.account.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SpringDataAccountJpaRepository
        extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByIban(String iban);

    @Query("""
        select distinct a
        from AccountEntity a
        join a.owners o
        where o.userId = :userId
    """)
    List<AccountEntity> findAllByUserId(Long userId);
}
