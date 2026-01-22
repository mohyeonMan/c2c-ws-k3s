package com.c2c.ws.application.service;

import org.springframework.stereotype.Service;

import com.c2c.ws.adapter.in.ws.dto.ClientFrame;
import com.c2c.ws.adapter.in.ws.dto.FrameType;
import com.c2c.ws.application.model.Ack;
import com.c2c.ws.application.port.in.ws.FrameHandler;
import com.c2c.ws.common.util.IdGenerator;

@Service
public class AckFrameHandler implements FrameHandler {
    
    @Override
    public FrameType supports() {
        return FrameType.ACK;
    }

    @Override
    public void handle(String userId, ClientFrame ackFrame) {

        Ack ack = Ack.builder()
                .ackId(IdGenerator.generateId("ack"))
                .eventId(ackFrame.)
                .action(ackFrame.getAction())
                .build();

    }
    
}
