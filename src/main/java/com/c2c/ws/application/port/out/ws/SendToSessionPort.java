package com.c2c.ws.application.port.out.ws;

public interface SendToSessionPort {
    void sendToSession(String sessionId, String payload);
}
