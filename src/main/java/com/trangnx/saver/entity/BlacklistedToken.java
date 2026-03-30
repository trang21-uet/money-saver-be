package com.trangnx.saver.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "blacklisted_tokens")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 1000)
    private String token;

    @Column(name = "blacklisted_at")
    private LocalDateTime blacklistedAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "user_email")
    private String userEmail;
}
