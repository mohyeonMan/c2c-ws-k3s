package com.c2c.ws.adapter.out.ws;

import org.springframework.stereotype.Component;

import com.c2c.ws.adapter.out.ws.dto.SFrame;
import com.c2c.ws.application.port.out.ws.SendToSessionPort;

@Component
public class SendToSessionAdapter implements SendToSessionPort {

    @Override
    public void sendToSession(String userId, SFrame payload) {

    }
    
}
