package com.c2c.ws.application.port.in.ws.frame;

import com.c2c.ws.adapter.in.ws.dto.CFrame;
import com.c2c.ws.adapter.in.ws.dto.CFrameType;

public interface FrameHandler {
    
    CFrameType supports();

    void handle(String userId, CFrame frame);

}
