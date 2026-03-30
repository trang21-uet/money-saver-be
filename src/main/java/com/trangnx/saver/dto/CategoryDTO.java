package com.trangnx.saver.dto;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
public class CategoryDTO {
    private Long id;
    private String name;
    private String type;
    private String icon;
    private String color;
    private Boolean isDefault;
}
