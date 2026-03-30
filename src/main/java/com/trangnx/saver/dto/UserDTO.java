package com.trangnx.saver.dto;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private String avatarUrl;
    private String provider;
    private Boolean isActive;
}
