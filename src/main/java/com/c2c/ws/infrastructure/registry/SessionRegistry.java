package com.c2c.ws.infrastructure.registry;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import com.c2c.ws.infrastructure.registry.model.Conn;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;

@Component
public class SessionRegistry {
    private final Cache<String, Conn> sessionsByUserId;

    public SessionRegistry(@Value("${ws.session.ttl-seconds:120}") long ttlSeconds) {
        RemovalListener<String, Conn> removalListener = (key, conn, cause) -> {
            if (cause == RemovalCause.EXPIRED && conn != null) {
                // Expired by TTL: treat as stale session cleanup.
            }
        };
        this.sessionsByUserId = Caffeine.newBuilder()
                .expireAfterAccess(Duration.ofSeconds(ttlSeconds))
                .removalListener(removalListener)
                .build();
    }

    public Conn register(String userId, WebSocketSession session) {
        Conn conn = Conn.builder()
                .connId(session.getId())
                .userId(userId)
                .session(session)
                .connectedAt(Instant.now())
                .lastSeenAt(Instant.now())
                .build();
        sessionsByUserId.put(userId, conn);

        return conn;
    }

    public Optional<Conn> find(String userId) {
        return Optional.ofNullable(sessionsByUserId.getIfPresent(userId));
    }

    public boolean touch(String userId) {
        Conn existing = sessionsByUserId.getIfPresent(userId);
        if (existing == null) {
            return false;
        }
        existing.setLastSeenAt(Instant.now());
        return true;
    }

    public void remove(String userId) {
        sessionsByUserId.invalidate(userId);
    }
}
