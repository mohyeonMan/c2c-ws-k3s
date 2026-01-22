package com.c2c.ws.application.port.in.ws;

import com.c2c.ws.adapter.in.ws.dto.ClientFrame;

public interface FrameDispatcherUseCase {

    void dispatchFrame(String userId,ClientFrame frame);
    
}
