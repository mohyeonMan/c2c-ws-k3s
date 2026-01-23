package com.c2c.ws.adapter.out.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.c2c.ws.adapter.out.ws.dto.SFrame;
import com.c2c.ws.application.port.out.ws.SendToSessionPort;
import com.c2c.ws.common.util.CommonMapper;
import com.c2c.ws.infrastructure.registry.SessionRegistry;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendToSessionAdapter implements SendToSessionPort {
    private final SessionRegistry registry;
    private final CommonMapper commonMapper;

    @Override
    public void sendToSession(String userId, SFrame serverFrame) {
        registry.find(userId).ifPresentOrElse(conn -> {
            WebSocketSession session = conn.getSession();
            if (session == null || !session.isOpen()) {
                log.warn("sendToSession: closed session userId={}", userId);
                registry.remove(userId);
                return;
            }

            try {
                String json = commonMapper.write(serverFrame);
                session.sendMessage(new TextMessage(json));
            } catch (Exception e) {
                log.warn("sendToSession failed: userId={}", userId, e);
            }

        }, () -> log.warn("sendToSession: no session userId={}", userId));
    }
    
}
