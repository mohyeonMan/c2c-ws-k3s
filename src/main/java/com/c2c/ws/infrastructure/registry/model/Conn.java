package com.c2c.ws.infrastructure.registry.model;

import java.time.Instant;

import org.springframework.web.socket.WebSocketSession;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class Conn {
    private final String connId;
    private final String userId;
    private final WebSocketSession session;
    private final Instant connectedAt;
    @Setter
    private Instant lastSeenAt;
}