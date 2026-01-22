package com.bank.account.domain;

import java.util.Objects;

public class AccountOwner {

    private final Long userId;

    public AccountOwner(Long userId) {
        this.userId = Objects.requireNonNull(userId);
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountOwner that)) return false;
        return userId.equals(that.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }
}
