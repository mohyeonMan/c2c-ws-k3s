package com.c2c.ws.adapter.in.ws;

import java.util.HashMap;
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
import com.c2c.ws.infrastructure.registry.model.Conn;

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

        log.debug("WS message received: sessionId={}, userId={}, type={}, action={}, requestId={}",
                session.getId(),
                userId,
                frame.getType(),
                frame.getAction(),
                frame.getRequestId());

        frameDispatcherUseCase.dispatchFrame(userId, frame);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        log.debug("WS connection established: sessionId={}", session.getId());
        Conn conn = sessionLifecycleUseCase.onOpen(session);
        String userId = conn.getUserId();

        CFrame frame = CFrame.builder()
                .requestId(IdGenerator.generateId("sys-req"))
                .type(CFrameType.SYSTEM)
                .action(Action.CONN_OPENED)
                .payload(commonMapper.write(
                    Map.of(
                        "sessionId", session.getId(),
                        "userId", userId,
                        "connId", conn.getConnId()
                    )
                ))
                .build();
        frameDispatcherUseCase.dispatchFrame(userId, frame);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String userId = sessionUserIdResolver.resolve(session);
        log.debug("WS connection closed: sessionId={}, userId={}, code={}, reason={}",
                session.getId(),
                userId,
                status.getCode(),
                status.getReason());
        CFrame frame = CFrame.builder()
                .requestId(IdGenerator.generateId("sys-req"))
                .type(CFrameType.SYSTEM)
                .action(Action.CONN_CLOSED)
                .payload(commonMapper.write(buildConnClosedPayload(status)))
                .build();
        frameDispatcherUseCase.dispatchFrame(userId, frame);
        sessionLifecycleUseCase.onClose(session);

    }

    private Map<String, Object> buildConnClosedPayload(CloseStatus status) {
        Map<String, Object> payload = new HashMap<>();
        if (status == null) {
            return payload;
        }
        payload.put("code", status.getCode());
        if (status.getReason() != null) {
            payload.put("reason", status.getReason());
        }
        return payload;
    }
}
