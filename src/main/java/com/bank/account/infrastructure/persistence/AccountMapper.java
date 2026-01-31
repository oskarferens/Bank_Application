package com.bank.account.infrastructure.persistence;

import com.bank.account.domain.*;

import java.util.Set;
import java.util.stream.Collectors;

public final class AccountMapper {

    private AccountMapper() {
    }

    // DOMAIN TO JPA
    public static AccountEntity toEntity(Account account) {
        AccountEntity entity = new AccountEntity();
        entity.setIban(account.getIban());
        entity.setCurrency(account.getCurrency().name());
        entity.setBalance(account.getBalance());
        entity.setDailyLimit(account.dailyLimitSnapshot());
        entity.setStatus(AccountStatusJpa.valueOf(account.getStatus().name()));

        entity.setOwners(
                account.getOwners().stream()
                        .map(owner -> {
                            AccountOwnerEntity ownerEntity = new AccountOwnerEntity();
                            ownerEntity.setUserId(owner.getUserId());
                            ownerEntity.setAccount(entity);
                            return ownerEntity;
                        })
                        .collect(Collectors.toSet())
        );

        return entity;
    }

    public static AccountEntity updateEntity(AccountEntity entity, Account account) {

        entity.setBalance(account.getBalance());
        entity.setDailyLimit(account.dailyLimitSnapshot());
        entity.setStatus(AccountStatusJpa.valueOf(account.getStatus().name()));

        entity.getOwners().clear();

        account.getOwners().forEach(owner -> {
            AccountOwnerEntity ownerEntity = new AccountOwnerEntity();
            ownerEntity.setUserId(owner.getUserId());
            ownerEntity.setAccount(entity);
            entity.getOwners().add(ownerEntity);
        });

        return entity;
    }


    // JPA TO DOMAIN
    public static Account toDomain(AccountEntity entity) {
        Set<AccountOwner> owners = entity.getOwners().stream()
                .map(o -> new AccountOwner(o.getUserId()))
                .collect(Collectors.toSet());

        return new Account(
                entity.getIban(),
                AccountCurrency.valueOf(entity.getCurrency()),
                entity.getBalance(),
                entity.getDailyLimit(),
                AccountStatus.valueOf(entity.getStatus().name()),
                owners
        );
    }
}
