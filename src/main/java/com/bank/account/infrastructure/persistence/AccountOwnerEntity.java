package com.bank.account.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account_owners")
@Getter
@Setter
public class AccountOwnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign Key to accounts.id
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    private AccountEntity account;

    // Foreign Key to users.id (without JPA relation)
    @Column(name = "user_id", nullable = false)
    private Long userId;
}
