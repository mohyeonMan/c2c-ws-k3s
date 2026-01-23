package com.c2c.ws.application.port.in.ws.frame;

import com.c2c.ws.adapter.in.ws.dto.CFrame;

public interface FrameDispatcherUseCase {

    void dispatchFrame(String userId, CFrame frame);
    
}
