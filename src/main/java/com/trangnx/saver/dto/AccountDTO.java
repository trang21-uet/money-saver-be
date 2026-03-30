package com.trangnx.saver.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
public class AccountDTO {
    private Long id;
    private String name;
    private String type;
    private BigDecimal balance;
    private String currency;
    private String icon;
    private String color;
    private Boolean isDefault;
}
