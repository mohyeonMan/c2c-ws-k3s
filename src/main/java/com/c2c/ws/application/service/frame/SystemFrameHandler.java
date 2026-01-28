package com.c2c.ws.application.service.frame;

import java.time.Instant;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.c2c.ws.adapter.in.ws.dto.CFrame;
import com.c2c.ws.adapter.in.ws.dto.CFrameType;
import com.c2c.ws.adapter.out.ws.dto.SFrame;
import com.c2c.ws.application.model.Action;
import com.c2c.ws.application.model.Command;
import com.c2c.ws.application.model.EventType;
import com.c2c.ws.application.model.Status;
import com.c2c.ws.application.port.in.ws.frame.FrameHandler;
import com.c2c.ws.application.port.out.mq.PublishCommandPort;
import com.c2c.ws.application.port.out.ws.SendToSessionPort;
import com.c2c.ws.common.util.CommonMapper;
import com.c2c.ws.common.util.IdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemFrameHandler implements FrameHandler {
    private final PublishCommandPort publishCommandPort;
    private final SendToSessionPort sendToSessionPort;
    private final CommonMapper commonMapper;


    @Override
    public CFrameType supports() {
        return CFrameType.SYSTEM;
    }

    @Override
    public void handle(String userId, CFrame frame) {
        log.info("handle system frame: userId={}, action={}, requestId={}",
                userId,
                frame.getAction(),
                frame.getRequestId());

                
        Action action = frame.getAction();
        if (action == null) {
            log.warn("system frame missing action: requestId={}", frame.getRequestId());
            return;
        }

        switch (action) {
            case CONN_OPENED:
                handleConnOpened(userId, frame);
                break;
            case CONN_CLOSED:
                handleConnClosed(userId, frame);
                break;
            default:
                throw new RuntimeException("Unsupported system frame action: " + action);
        }
    }

    private void handleConnOpened(String userId, CFrame frame) {
        if (userId == null || userId.isBlank()) {
            log.warn("CONN_OPENED missing userId: requestId={}", frame.getRequestId());
            return;
        }
        SFrame response = SFrame.builder()
                .requestId(frame.getRequestId())
                .resId(IdGenerator.generateId("sys-res"))
                .type(EventType.SYSTEM)
                .action(Action.CONN_OPENED)
                .status(Status.DELIVERED)
                .payload(commonMapper.write(Map.of("userId", userId)))
                .build();

        sendToSessionPort.sendToSession(userId, response);
    }

    private void handleConnClosed(String userId, CFrame frame) {
        Command command = Command.builder()
                .requestId(frame.getRequestId())
                .commandId(IdGenerator.generateId("sys-cmd"))
                .userId(userId)
                .action(frame.getAction())
                .payload(frame.getPayload())
                .sentAt(Instant.now())
                .build();

        log.debug("system command built: commandId={}, requestId={}, userId={}, action={}",
                command.getCommandId(),
                command.getRequestId(),
                command.getUserId(),
                command.getAction());

        publishCommandPort.publishCommand(command);
    }
}
