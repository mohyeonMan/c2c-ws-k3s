package com.c2c.ws.adapter.in.ws;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.c2c.ws.adapter.in.ws.dto.CFrame;
import com.c2c.ws.adapter.in.ws.dto.CFrameType;
import com.c2c.ws.application.model.Action;
import com.c2c.ws.application.port.in.ws.frame.FrameDispatcherUseCase;
import com.c2c.ws.application.port.in.ws.session.SessionLifecycleUseCase;
import com.c2c.ws.application.service.session.SessionUserIdResolver;
import com.c2c.ws.common.util.CommonMapper;
import com.c2c.ws.common.util.IdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
@RequiredArgsConstructor
public class WsMessageHandler extends TextWebSocketHandler {
    private final SessionUserIdResolver sessionUserIdResolver;
    private final SessionLifecycleUseCase sessionLifecycleUseCase;
    private final FrameDispatcherUseCase frameDispatcherUseCase;
    private final CommonMapper commonMapper;


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        String userId = sessionUserIdResolver.resolve(session);
        CFrame frame = commonMapper.read(message.getPayload(), CFrame.class);

        if (frame == null) {
            log.warn("WS message parse failed: sessionId={}, userId={}, payloadSize={}",
                    session.getId(),
                    userId,
                    message.getPayloadLength());
            return;
        }

        log.info("WS message received: sessionId={}, userId={}, type={}, action={}, requestId={}",
                session.getId(),
                userId,
                frame.getType(),
                frame.getAction(),
                frame.getRequestId());

        frameDispatcherUseCase.dispatchFrame(userId, frame);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.info("WS connection established: sessionId={}", session.getId());
        sessionLifecycleUseCase.onOpen(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = sessionUserIdResolver.resolve(session);
        log.info("WS connection closed: sessionId={}, userId={}, code={}, reason={}",
                session.getId(),
                userId,
                status.getCode(),
                status.getReason());
        CFrame frame = CFrame.builder()
                .requestId(IdGenerator.generateId("sys-req"))
                .type(CFrameType.SYSTEM)
                .action(Action.CONN_CLOSED)
                .payload(commonMapper.write(Map.of(
                        "code", status.getCode(),
                        "reason", status.getReason()
                )))
                .build();
        frameDispatcherUseCase.dispatchFrame(userId, frame);
        sessionLifecycleUseCase.onClose(session);

    }
}
