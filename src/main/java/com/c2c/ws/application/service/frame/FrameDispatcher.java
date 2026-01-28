package com.c2c.ws.application.service.frame;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

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
        if (frame == null) {
            log.warn("dispatchFrame: frame is null userId={}", userId);
            return;
        }

        FrameHandler handler = frameHandlerMap.get(frame.getType());
        if (handler == null) {
            log.warn("dispatchFrame: no handler userId={}, type={}, requestId={}",
                    userId,
                    frame.getType(),
                    frame.getRequestId());
            return;
        }

        log.info("dispatchFrame: userId={}, type={}, action={}, requestId={}, handler={}",
                userId,
                frame.getType(),
                frame.getAction(),
                frame.getRequestId(),
                handler.getClass().getSimpleName());
        handler.handle(userId, frame);
    }
}
