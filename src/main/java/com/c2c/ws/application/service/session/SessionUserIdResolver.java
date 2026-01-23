package com.c2c.ws.application.service.session;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class SessionUserIdResolver {
    private static final String SESSION_KEY = "USER";

    public String resolve(WebSocketSession session) {
        if (session == null) {
            return null;
        }
        Object stored = session.getAttributes().get(SESSION_KEY);
        String userId = stored instanceof String ? (String) stored : null;
        if (userId != null && !userId.isBlank()) {
            return userId;
        }
        userId = session.getHandshakeHeaders().getFirst("X-User-Id");
        if (userId != null && !userId.isBlank()) {
            return userId;
        }
        return resolveFromQuery(session);
    }

    public void store(WebSocketSession session, String userId) {
        if (session == null || userId == null || userId.isBlank()) {
            return;
        }
        session.getAttributes().put(SESSION_KEY, userId);
    }

    public void clear(WebSocketSession session) {
        if (session == null) {
            return;
        }
        session.getAttributes().remove(SESSION_KEY);
    }

    private String resolveFromQuery(WebSocketSession session) {
        if (session == null || session.getUri() == null) {
            return null;
        }
        String query = session.getUri().getQuery();
        if (query == null || query.isBlank()) {
            return null;
        }
        for (String part : query.split("&")) {
            if (part == null || part.isBlank()) {
                continue;
            }
            String[] kv = part.split("=", 2);
            if (kv.length == 0) {
                continue;
            }
            if ("userId".equals(kv[0])) {
                return kv.length > 1 ? kv[1] : null;
            }
        }
        return null;
    }
}
