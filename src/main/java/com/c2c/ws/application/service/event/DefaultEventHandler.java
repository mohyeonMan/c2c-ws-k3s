package com.c2c.ws.application.service.event;

import org.springframework.stereotype.Service;

import com.c2c.ws.adapter.out.ws.dto.SFrame;
import com.c2c.ws.application.model.Event;
import com.c2c.ws.application.model.EventType;
import com.c2c.ws.application.port.in.mq.EventHandler;
import com.c2c.ws.application.port.out.ws.SendToSessionPort;
import com.c2c.ws.common.util.IdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultEventHandler implements EventHandler {

    private final SendToSessionPort sendToSessionPort;

    @Override
    public void handle(Event event) {
        if (event == null) {
            log.warn("event is null");
            return;
        }
        if (event.getType() == null || event.getType() == EventType.UNKNOWN) {
            log.warn("event type unknown: {}", event);
            return;
        }

        String userId = event.getUserId();
        if (userId == null || userId.isBlank()) {
            log.warn("event missing userId: {}", event);
            return;
        }

        log.info("HANDLE EVENT :: {}", event);

        SFrame frame = SFrame.builder()
                .requestId(event.getRequestId())
                .resId(IdGenerator.generateId("res"))
                .eventId(event.getEventId())
                .type(event.getType())
                .action(event.getAction())
                .status(event.getStatus())
                .payload(event.getPayload())
                .build();

        sendToSessionPort.sendToSession(userId, frame);
    }

}
