package com.trangnx.saver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
    private Long id;
    private String name;
    private String type; // INCOME or EXPENSE
    private String icon;
    private String color;
    private Boolean isDefault;
}