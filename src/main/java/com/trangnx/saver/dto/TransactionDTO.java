package com.trangnx.saver.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
public class TransactionDTO {
    private Long id;
    private Long accountId;
    private String accountName;
    private Long categoryId;
    private String categoryName;
    private String type;
    private BigDecimal amount;
    private String description;
    private LocalDate transactionDate;
    private String notes;
    private String tags;
}
