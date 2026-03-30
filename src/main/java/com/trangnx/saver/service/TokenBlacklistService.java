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
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void blacklistToken(String token) {
        Date expiration = jwtService.extractExpiration(token);
        long ttl = expiration.getTime() - System.currentTimeMillis();
        if (ttl > 0) {
            redisTemplate.opsForValue().set(BLACKLIST_PREFIX + token, "1", Duration.ofMillis(ttl));
        }
    }

    public boolean isBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + token));
    }

    public void removeFromBlacklist(String token) {
        redisTemplate.delete(BLACKLIST_PREFIX + token);
    }

    public long getBlacklistedTokensCount() {
        var keys = redisTemplate.keys(BLACKLIST_PREFIX + "*");
        return keys != null ? keys.size() : 0;
    }
}
