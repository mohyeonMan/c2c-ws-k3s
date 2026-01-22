package com.c2c.ws.application.service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.c2c.ws.adapter.in.ws.dto.ClientFrame;
import com.c2c.ws.adapter.in.ws.dto.FrameType;
import com.c2c.ws.application.port.in.ws.FrameDispatcherUseCase;
import com.c2c.ws.application.port.in.ws.FrameHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FrameDispatcher implements FrameDispatcherUseCase {
    private final Map<FrameType, FrameHandler> frameHandlerMap;

    public FrameDispatcher(List<FrameHandler> handlers) {
        Objects.requireNonNull(handlers, "handlers");
        Map<FrameType, FrameHandler> mapped = new EnumMap<>(FrameType.class);
        for (FrameHandler handler : handlers) {
            if (handler == null || handler.supports() == null) {
                continue;
            }
            mapped.put(handler.supports(), handler);
        }
        this.frameHandlerMap = mapped;
    }


    @Override
    public void dispatchFrame(ClientFrame frame) {
        if (frame == null || frame.getType() == null) {
            log.warn("Frame or frame type is null.");
            return;
        }
        FrameHandler handler = frameHandlerMap.get(frame.getType());
        if (handler != null) {
            handler.process(frame);
        } else {
            log.warn("No handler found for frame type: {}", frame.getType());
        }
    }
}
