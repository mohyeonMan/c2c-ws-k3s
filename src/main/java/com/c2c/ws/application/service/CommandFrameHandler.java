package com.c2c.ws.application.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.c2c.ws.adapter.in.ws.dto.ClientFrame;
import com.c2c.ws.adapter.in.ws.dto.FrameType;
import com.c2c.ws.application.model.Command;
import com.c2c.ws.application.port.in.ws.FrameHandler;
import com.c2c.ws.application.port.out.mq.PublishCommandPort;
import com.c2c.ws.common.util.IdGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommandFrameHandler implements FrameHandler {
    private final PublishCommandPort publishCommandPort;

    @Override
    public FrameType supports() {
        return FrameType.COMMAND;
    }
    
    @Override
    public void handle(String userId, ClientFrame frame) {
    
        Command command = Command.builder()
                .userId(userId)
                .commandId(IdGenerator.generateId("cmd"))
                .requestId(frame.getRequestId())
                .action(frame.getAction())
                .payload(frame.getPayload())
                .sentAt(Instant.now())
                .build();

        publishCommandPort.publishCommand(command);            

    }   
    
}
