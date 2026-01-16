package com.trangnx.saver.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "google_id", unique = true)
    private String googleId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider provider = AuthProvider.GOOGLE;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public enum AuthProvider {
        GOOGLE,
        LOCAL
    }
}