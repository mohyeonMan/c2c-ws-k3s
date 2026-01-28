package com.c2c.ws.application.service.frame;

import org.springframework.stereotype.Service;

import com.c2c.ws.adapter.in.ws.dto.CFrame;
import com.c2c.ws.adapter.in.ws.dto.CFrameType;
import com.c2c.ws.application.port.out.ws.SendToSessionPort;
import com.c2c.ws.common.util.CommonMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FallbackFrameHandler extends AbstractCommandFrameHandler{
    
    public FallbackFrameHandler(
            SendToSessionPort sendToSessionPort,
            CommonMapper commonMapper
    ) {
        super(sendToSessionPort, commonMapper);
    }

    @Override
    public CFrameType supports() {
        return CFrameType.UNKNOWN;
    }

    @Override
    public void doHandle(String userId, CFrame frame) {
    
        log.warn("handle fallback frame: userId={}, type={}, action={}, requestId={}",
                userId,
                frame.getType(),
                frame.getAction(),
                frame.getRequestId());
        
        throw new RuntimeException();

    }   
}
