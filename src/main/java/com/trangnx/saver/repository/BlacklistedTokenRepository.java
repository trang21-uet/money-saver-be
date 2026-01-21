package com.trangnx.saver.repository;

import com.trangnx.saver.entity.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {

    boolean existsByToken(String token);

    Optional<BlacklistedToken> findByToken(String token);

    void deleteByExpiresAtBefore(LocalDateTime dateTime);
}