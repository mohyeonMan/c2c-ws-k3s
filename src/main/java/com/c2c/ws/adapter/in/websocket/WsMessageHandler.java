package com.c2c.ws.adapter.in.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.c2c.ws.application.port.out.PublishMessagePort;

@Component
public class WsMessageHandler extends TextWebSocketHandler {

    private final PublishMessagePort publishMessagePort;

    public WsMessageHandler(PublishMessagePort publishMessagePort) {
        this.publishMessagePort = publishMessagePort;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        publishMessagePort.publish(message.getPayload());
    }
}
