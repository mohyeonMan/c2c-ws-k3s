package com.c2c.ws.application.service.frame;


import com.c2c.ws.adapter.in.ws.dto.CFrame;
import com.c2c.ws.adapter.out.ws.dto.SFrame;
import com.c2c.ws.application.model.EventType;
import com.c2c.ws.application.model.Status;
import com.c2c.ws.application.port.in.ws.frame.FrameHandler;
import com.c2c.ws.application.port.out.ws.SendToSessionPort;
import com.c2c.ws.common.util.CommonMapper;
import com.c2c.ws.common.util.IdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCommandFrameHandler implements FrameHandler{
    private final SendToSessionPort sendToSessionPort;
    private final CommonMapper commonMapper;

    @Override
    public void handle(String userId, CFrame frame) {
        try {
            log.debug("handle command frame: userId={}, type={}, action={}, requestId={}",
                    userId,
                    frame.getType(),
                    frame.getAction(),
                    frame.getRequestId());
            sendAck(userId, frame);
            doHandle(userId, frame);
        } catch (Exception e) {
            log.error("ERROR : {}",e.getMessage(),e);
            sendErrorResult(userId, frame, e);
        }
    }

    protected abstract void doHandle(String userId, CFrame frame);

    protected void sendAck(String userID, CFrame frame) {
        SFrame ack = buildAck(frame);
        log.debug("sendAck: userId={}, requestId={}, resId={}",
                userID,
                ack.getRequestId(),
                ack.getResId());
        sendToSessionPort.sendToSession(userID, ack);
    }

    protected void sendErrorResult(String userId, CFrame frame, Exception ex) {
        // String payload = errorResponseFactory.payload(ex, true);

        String payload = null;
        SFrame response = buildResult(frame, Status.ERROR, payload);
        log.debug("sendErrorResult: userId={}, requestId={}, resId={}, error={}",
                userId,
                response.getRequestId(),
                response.getResId(),
                ex.getMessage());
        sendToSessionPort.sendToSession(userId, response);
    }

    protected SFrame buildResult(
            CFrame frame,
            Status status,
            String payload
    ) {
        return buildServerFrame(frame, EventType.RESULT, status, payload);
    }

    protected SFrame buildAck(CFrame frame){
        return buildServerFrame(frame, EventType.ACK, Status.DELIVERED);
    }

    protected SFrame buildServerFrame(
            CFrame frame,
            EventType type,
            Status status
    ) {
        return buildServerFrame(frame, type, status, null);
    }

    protected SFrame buildServerFrame(
            CFrame frame,
            EventType type,
            Status status,
            Object payload
    ) {
        SFrame serverFrame = SFrame.builder()
                .requestId(frame.getRequestId())
                .resId(IdGenerator.generateId("sf"))
                .type(type)
                .status(status)
                .payload(commonMapper.write(payload))
                .build();
        return serverFrame;
    }

}
