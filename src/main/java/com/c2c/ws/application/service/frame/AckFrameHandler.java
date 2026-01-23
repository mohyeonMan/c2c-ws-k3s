package com.c2c.ws.application.service.frame;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.c2c.ws.adapter.in.ws.dto.CFrame;
import com.c2c.ws.adapter.in.ws.dto.CFrameType;
import com.c2c.ws.application.model.Ack;
import com.c2c.ws.application.port.in.ws.frame.FrameHandler;
import com.c2c.ws.application.port.out.mq.PublishAckPort;
import com.c2c.ws.common.util.IdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AckFrameHandler implements FrameHandler {
    private final PublishAckPort publishAckPort;

    @Override
    public CFrameType supports() {
        return CFrameType.ACK;
    }

    @Override
    public void handle(String userId, CFrame frame) {
        
        log.info("handle ack :: userId={}, frame={}",userId, frame.toString());

        Ack ack = Ack.builder()
                .ackId(IdGenerator.generateId("ack"))
                .eventId(frame.getEventId())
                .action(frame.getAction())
                .sentAt(Instant.now())
                .build();

        publishAckPort.publishAck(ack);
    }
    
}
