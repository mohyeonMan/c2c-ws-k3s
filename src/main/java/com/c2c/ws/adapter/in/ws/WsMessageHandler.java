package com.c2c.ws.adapter.in.ws;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.c2c.ws.adapter.in.ws.dto.CFrame;
import com.c2c.ws.application.port.in.ws.frame.FrameDispatcherUseCase;
import com.c2c.ws.application.port.in.ws.session.SessionLifecycleUseCase;
import com.c2c.ws.application.service.session.SessionUserIdResolver;
import com.c2c.ws.common.util.CommonMapper;

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

        frameDispatcherUseCase.dispatchFrame(userId, frame);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionLifecycleUseCase.onOpen(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionLifecycleUseCase.onClose(session);
    }
}
