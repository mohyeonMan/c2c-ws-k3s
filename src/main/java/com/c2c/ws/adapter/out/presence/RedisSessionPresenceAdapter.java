package com.c2c.ws.adapter.out.presence;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.c2c.ws.application.port.out.presence.SessionPresencePort;

@Component
public class RedisSessionPresenceAdapter implements SessionPresencePort {
    private static final String KEY_PREFIX = "presence:session:";

    private final StringRedisTemplate redisTemplate;
    private final Duration ttl;

    public RedisSessionPresenceAdapter(
            StringRedisTemplate redisTemplate,
            @Value("${ws.presence.ttl-seconds:120}") long ttlSeconds
    ) {
        this.redisTemplate = redisTemplate;
        this.ttl = Duration.ofSeconds(ttlSeconds);
    }

    @Override
    public void markSessionActive(String userId, String nodeQueue) {
        if (userId == null || userId.isBlank()) {
            return;
        }
        if (nodeQueue == null || nodeQueue.isBlank()) {
            return;
        }
        redisTemplate.opsForValue().set(key(userId), nodeQueue, ttl);
    }

    @Override
    public void markSessionInactive(String userId) {
        if (userId == null || userId.isBlank()) {
            return;
        }
        redisTemplate.delete(key(userId));
    }

    @Override
    public boolean isSessionActive(String userId) {
        if (userId == null || userId.isBlank()) {
            return false;
        }
        Boolean exists = redisTemplate.hasKey(key(userId));
        return Boolean.TRUE.equals(exists);
    }

    private String key(String userId) {
        return KEY_PREFIX + userId;
    }
}
