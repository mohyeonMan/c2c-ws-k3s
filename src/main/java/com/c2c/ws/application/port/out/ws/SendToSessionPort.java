package com.c2c.ws.application.port.out.ws;

import com.c2c.ws.adapter.out.ws.dto.SFrame;

public interface SendToSessionPort {
    void sendToSession(String userId, SFrame frame);
}
