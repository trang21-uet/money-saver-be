package com.trangnx.saver.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "google_id")
    private String googleId;

    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    public enum AuthProvider {
        GOOGLE, LOCAL
    }
}
