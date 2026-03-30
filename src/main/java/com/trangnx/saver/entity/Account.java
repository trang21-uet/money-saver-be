package com.trangnx.saver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType type;

    @Column(precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;

    private String currency;
    private String icon;
    private String color;

    @Column(name = "is_default")
    @Builder.Default
    private Boolean isDefault = false;

    public enum AccountType {
        WALLET, BANK, SAVINGS_ACCOUNT, CREDIT_CARD
    }
}
