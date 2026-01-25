package com.c2c.ws.application.service.frame;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.c2c.ws.adapter.in.ws.dto.CFrame;
import com.c2c.ws.adapter.in.ws.dto.CFrameType;
import com.c2c.ws.application.model.Command;
import com.c2c.ws.application.port.in.ws.frame.FrameHandler;
import com.c2c.ws.application.port.out.mq.PublishCommandPort;
import com.c2c.ws.common.util.IdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemFrameHandler implements FrameHandler {
    private final PublishCommandPort publishCommandPort;

    @Override
    public CFrameType supports() {
        return CFrameType.SYSTEM;
    }

    @Override
    public void handle(String userId, CFrame frame) {
        
        log.info("handle system frame :: userId={}, frame={}",userId, frame.toString());
        Command command = Command.builder()
                .requestId(frame.getRequestId())
                .commandId(IdGenerator.generateId("sys-cmd"))
                .userId(userId)
                .action(frame.getAction())
                .payload(frame.getPayload())
                .sentAt(Instant.now())
                .build();

        publishCommandPort.publishCommand(command);
    }
}
