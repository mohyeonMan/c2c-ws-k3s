package com.c2c.ws.adapter.out.ws;

import com.c2c.ws.application.port.out.ws.SendToSessionPort;

public class SendToSessionAdapter implements SendToSessionPort {

    @Override
    public void sendToSession(String sessionId, String payload) {
        // Implementation to send payload to the specified WebSocket session
    }
    
}
