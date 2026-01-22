package com.c2c.ws.application.port.in.ws;

import com.c2c.ws.adapter.in.ws.dto.ClientFrame;
import com.c2c.ws.adapter.in.ws.dto.FrameType;

public interface FrameHandler {
    
    FrameType supports();

    void handle(String userId, ClientFrame command);

}
