package com.trangnx.saver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(nullable = false, length = 3)
    private String currency = "VND";

    @Column(length = 50)
    private String icon;

    @Column(length = 20)
    private String color;

    @Column(name = "is_default", nullable = false)
    private Boolean isDefault = false;

    public enum AccountType {
        CASH,
        BANK,
        E_WALLET,
        CREDIT_CARD
    }
}