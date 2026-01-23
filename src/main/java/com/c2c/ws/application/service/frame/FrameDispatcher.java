package com.c2c.ws.application.service.frame;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.c2c.ws.adapter.in.ws.dto.CFrame;
import com.c2c.ws.adapter.in.ws.dto.CFrameType;
import com.c2c.ws.application.port.in.ws.frame.FrameDispatcherUseCase;
import com.c2c.ws.application.port.in.ws.frame.FrameHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FrameDispatcher implements FrameDispatcherUseCase {
    private final Map<CFrameType, FrameHandler> frameHandlerMap;

    public FrameDispatcher(List<FrameHandler> handlers) {
        Objects.requireNonNull(handlers, "handlers");
        Map<CFrameType, FrameHandler> mapped = new EnumMap<>(CFrameType.class);
        for (FrameHandler handler : handlers) {
            if (handler == null || handler.supports() == null) {
                continue;
            }
            mapped.put(handler.supports(), handler);
        }
        this.frameHandlerMap = mapped;
    }


    @Override
    public void dispatchFrame(String userId, CFrame frame) {
        if (frame == null || frame.getType() == null) {
            log.warn("Frame or frame type is null.");
            return;
        }
        FrameHandler handler = frameHandlerMap.get(frame.getType());
        if (handler != null) {
            handler.handle(userId, frame);
        } else {
            log.warn("No handler found for frame type: {}", frame.getType());
        }
    }
}
