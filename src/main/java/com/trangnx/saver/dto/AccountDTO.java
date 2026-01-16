package com.trangnx.saver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private Long id;
    private String name;
    private String type; // CASH, BANK, E_WALLET, CREDIT_CARD
    private BigDecimal balance;
    private String currency;
    private String icon;
    private String color;
    private Boolean isDefault;
}