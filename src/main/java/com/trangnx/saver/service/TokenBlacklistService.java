package com.trangnx.saver.service;

import com.trangnx.saver.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtService jwtService;

    private static final String BLACKLIST_PREFIX = "blacklist:token:";

    /**
     * Add token to blacklist (invalidate token)
     */
    public void blacklistToken(String token) {
        try {
            // Extract expiration date from token
            Date expirationDate = jwtService.extractExpiration(token);
            long ttl = expirationDate.getTime() - System.currentTimeMillis();

            if (ttl > 0) {
                String key = BLACKLIST_PREFIX + token;
                String email = jwtService.extractEmail(token);

                // Store in Redis with TTL (auto-expire when token expires)
                redisTemplate.opsForValue().set(
                        key,
                        email,
                        Duration.ofMillis(ttl)
                );

                System.out.println("DEBUG: Token blacklisted in Redis for user: " + email + " (TTL: " + ttl + "ms)");
            }
        } catch (Exception e) {
            System.out.println("DEBUG: Failed to blacklist token in Redis: " + e.getMessage());
        }
    }

    /**
     * Check if token is blacklisted
     */
    public boolean isBlacklisted(String token) {
        try {
            String key = BLACKLIST_PREFIX + token;
            Boolean exists = redisTemplate.hasKey(key);
            return Boolean.TRUE.equals(exists);
        } catch (Exception e) {
            System.out.println("DEBUG: Error checking blacklist: " + e.getMessage());
            // If Redis is down, deny access for safety
            return false;
        }
    }

    /**
     * Remove token from blacklist (rarely used, mainly for testing)
     */
    public void removeFromBlacklist(String token) {
        try {
            String key = BLACKLIST_PREFIX + token;
            redisTemplate.delete(key);
            System.out.println("DEBUG: Token removed from blacklist");
        } catch (Exception e) {
            System.out.println("DEBUG: Failed to remove token from blacklist: " + e.getMessage());
        }
    }

    /**
     * Get total blacklisted tokens count (for monitoring)
     */
    public long getBlacklistedTokensCount() {
        try {
            var keys = redisTemplate.keys(BLACKLIST_PREFIX + "*");
            return keys != null ? keys.size() : 0;
        } catch (Exception e) {
            return 0;
        }
    }
}