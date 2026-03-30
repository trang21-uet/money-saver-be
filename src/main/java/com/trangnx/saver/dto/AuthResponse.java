package com.trangnx.saver.dto;

import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode @ToString
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String fullName;
    private Long userId;
    private String avatarUrl;
    private Long expiresIn;
}
