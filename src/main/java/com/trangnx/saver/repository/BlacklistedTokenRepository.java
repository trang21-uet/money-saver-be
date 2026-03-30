package com.trangnx.saver.repository;

import com.trangnx.saver.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    boolean existsByToken(String token);
    Optional<BlacklistedToken> findByToken(String token);
    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}
