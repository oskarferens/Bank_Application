package com.bank.account.infrastructure.persistence;

import com.bank.account.domain.Account;
import com.bank.account.domain.AccountOwner;

import java.util.stream.Collectors;

public class AccountMapper {

    public static AccountEntity toEntity(Account account) {
        AccountEntity entity = new AccountEntity();
        entity.setIban(account.getIban());
        entity.setCurrency("SEK");
        entity.setBalance(account.getBalance());
        entity.setDailyLimit(account.dailyLimitSnapshot());

        account.getOwners().forEach(owner -> {
            AccountOwnerEntity ownerEntity = new AccountOwnerEntity();
            ownerEntity.setAccount(entity);
            ownerEntity.setUserId(owner.getUserId());
            entity.getOwners().add(ownerEntity);
        });

        return entity;
    }

    public static Account toDomain(AccountEntity entity) {
        var owners = entity.getOwners().stream()
                .map(o -> new AccountOwner(o.getUserId()))
                .collect(Collectors.toList());

        Account account = new Account(
                entity.getIban(),
                owners.get(0)
        );

        owners.stream()
                .skip(1)
                .forEach(account::addOwner);

        account.deposit(entity.getBalance());

        return account;
    }
}
