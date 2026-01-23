package com.c2c.ws.application.service.frame;


import com.c2c.ws.adapter.in.ws.dto.CFrame;
import com.c2c.ws.adapter.out.ws.dto.SFrame;
import com.c2c.ws.adapter.out.ws.dto.SFrameType;
import com.c2c.ws.adapter.out.ws.dto.Status;
import com.c2c.ws.application.port.in.ws.frame.FrameHandler;
import com.c2c.ws.application.port.out.ws.SendToSessionPort;
import com.c2c.ws.common.util.CommonMapper;
import com.c2c.ws.common.util.IdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractFrameHandler implements FrameHandler{
    private final SendToSessionPort sendToSessionPort;
    private final CommonMapper commonMapper;

    @Override
    public void handle(String userId, CFrame frame) {
        try {
            ack(userId, frame);
            doHandle(userId, frame);
        } catch (Exception e) {
            sendErrorResult(userId, frame, e);
        }
    }

    protected abstract void doHandle(String userId, CFrame frame);

    protected void ack(String userID, CFrame frame) {
        SFrame ack = buildAck(frame);
        sendToSessionPort.sendToSession(userID, ack);
    }

    protected void sendErrorResult(String userId, CFrame frame, Exception ex) {
        // String payload = errorResponseFactory.payload(ex, true);

        String payload = null;
        SFrame response = buildResult(frame, Status.ERROR, payload);
        sendToSessionPort.sendToSession(userId, response);
    }

    protected SFrame buildResult(
            CFrame frame,
            Status status,
            String payload
    ) {
        return buildServerFrame(frame, SFrameType.RESULT, status, payload);
    }

    protected SFrame buildAck(CFrame frame){
        return buildServerFrame(frame, SFrameType.ACK, Status.DELIVERED);
    }

    protected SFrame buildServerFrame(
            CFrame frame,
            SFrameType type,
            Status status
    ) {
        return buildServerFrame(frame, type, status, null);
    }

    protected SFrame buildServerFrame(
            CFrame frame,
            SFrameType type,
            Status status,
            Object payload
    ) {
        SFrame serverFrame = SFrame.builder()
                .requestId(frame.getRequestId())
                .frameId(IdGenerator.generateId("sf"))
                .type(type)
                .status(status)
                .payload(commonMapper.write(payload))
                .build();
        return serverFrame;
    }

}
