package com.c2c.ws.adapter.out.presence;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.c2c.ws.application.port.out.presence.NodePresencePort;

import jakarta.annotation.PreDestroy;

@Component
public class RedisNodePresenceAdapter implements NodePresencePort {
    private static final String KEY_PREFIX = "presence:node:";

    private final StringRedisTemplate redisTemplate;
    private final Duration ttl;
    private final String nodeId;

    public RedisNodePresenceAdapter(
            StringRedisTemplate redisTemplate,
            @Value("${ws.node-presence.ttl-seconds:30}") long ttlSeconds,
            @Value("${ws.node-id}") String nodeId
    ) {
        this.redisTemplate = redisTemplate;
        this.ttl = Duration.ofSeconds(ttlSeconds);
        this.nodeId = nodeId;
    }

    @Override
    public void markNodeActive(String nodeId) {
        if (nodeId == null || nodeId.isBlank()) {
            return;
        }
        redisTemplate.opsForValue().set(key(nodeId), "1", ttl);
    }

    @Override
    public void markNodeInactive(String nodeId) {
        if (nodeId == null || nodeId.isBlank()) {
            return;
        }
        redisTemplate.delete(key(nodeId));
    }

    @Scheduled(fixedRateString = "${ws.node-presence.heartbeat-ms:10000}")
    public void heartbeat() {
        markNodeActive(nodeId);
    }

    @PreDestroy
    public void onShutdown() {
        markNodeInactive(nodeId);
    }

    private String key(String nodeId) {
        return KEY_PREFIX + nodeId;
    }
}
