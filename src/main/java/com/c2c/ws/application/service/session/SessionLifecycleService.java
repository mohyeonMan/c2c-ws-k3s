package com.c2c.ws.application.service.session;


import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.beans.factory.annotation.Value;

import com.c2c.ws.application.port.in.ws.session.SessionLifecycleUseCase;
import com.c2c.ws.application.port.out.presence.SessionPresencePort;
import com.c2c.ws.common.util.IdGenerator;
import com.c2c.ws.infrastructure.registry.SessionRegistry;
import com.c2c.ws.infrastructure.registry.model.Conn;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionLifecycleService implements SessionLifecycleUseCase{
    private final SessionPresencePort sessionPresencePort;
    private final SessionRegistry registry;
    private final SessionUserIdResolver userIdResolver;
    @Value("${ws.mq.event.routing-key}")
    private String nodeRoutingKey;

    @Override
    public Conn onOpen(WebSocketSession session) {
        String userId = userIdResolver.resolve(session);
        if (userId == null || userId.isBlank()) {
            userId = IdGenerator.generateId("user");
        }
        if (sessionPresencePort.isSessionActive(userId)) {
            log.info("onOpen: presence exists, issuing new userId. oldUserId={}", userId);
            userId = IdGenerator.generateId("user");
        }

        log.info("onOpen: sessionId={}, userId={}, routingKey={}",
                session.getId(),
                userId,
                nodeRoutingKey);

        Conn conn = registry.register(userId, session);
        sessionPresencePort.markSessionActive(userId, nodeRoutingKey);
        userIdResolver.store(session, userId);

        log.info("USER CONNECTED : {}", userId);
        return conn;
        
    }

    @Override
    public Conn onClose(WebSocketSession session) {
        if (session == null) {
            log.warn("onClose: session is null");
            return null;
        }

        String userId = userIdResolver.resolve(session);

        if (userId == null || userId.isBlank()) {
            log.warn("onClose without userId: sessionId={}", session.getId());
            return null;
        }

        log.info("onClose: sessionId={}, userId={}", session.getId(), userId);
        Conn conn = registry.find(userId).orElse(null);
        registry.remove(userId);
        sessionPresencePort.markSessionInactive(userId);
        userIdResolver.clear(session);

        log.info("USER DISCONNECTED : {}", userId);
        
        return conn;
    }

    @Override
    public void touch(String userId) {
        if(!registry.touch(userId)){
            log.warn("touch: closed conn userId={}", userId);
            throw new RuntimeException("CLOSED CONN");
        }
        log.debug("touch: userId={}, routingKey={}", userId, nodeRoutingKey);
        sessionPresencePort.markSessionActive(userId, nodeRoutingKey);
    }

}
