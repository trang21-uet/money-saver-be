package com.trangnx.saver.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
public class UserStatsDTO {
    private Long userId;
    private Long accountsCount;
    private Long categoriesCount;
    private Long transactionsCount;
    private BigDecimal totalBalance;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyExpense;
}
