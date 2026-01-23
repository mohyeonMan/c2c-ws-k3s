package com.c2c.ws.application.port.in.ws.session;

import org.springframework.web.socket.WebSocketSession;

import com.c2c.ws.infrastructure.registry.model.Conn;

public interface SessionLifecycleUseCase {
    
    Conn onOpen(WebSocketSession session);

    Conn onClose(WebSocketSession session);

    void touch(String userId);



}
