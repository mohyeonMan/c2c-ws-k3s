package com.c2c.ws.application.service.frame;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.c2c.ws.adapter.in.ws.dto.CFrame;
import com.c2c.ws.adapter.in.ws.dto.CFrameType;
import com.c2c.ws.application.model.Command;
import com.c2c.ws.application.port.in.ws.command.CommandDispatcherUseCase;
import com.c2c.ws.application.port.out.ws.SendToSessionPort;
import com.c2c.ws.common.util.CommonMapper;
import com.c2c.ws.common.util.IdGenerator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DefaultCommandFrameHandler extends AbstractCommandFrameHandler {

    private final CommandDispatcherUseCase commandDispatcherUseCase;

    public DefaultCommandFrameHandler(
            SendToSessionPort sendToSessionPort,
            CommonMapper commonMapper,
            CommandDispatcherUseCase commandDispatcherUseCase
    ) {
        super(sendToSessionPort, commonMapper);
        this.commandDispatcherUseCase = commandDispatcherUseCase;
    }


    @Override
    public CFrameType supports() {
        return CFrameType.COMMAND;
    }
    
    @Override
    public void doHandle(String userId, CFrame frame) {
    
        log.info("handle command frame: userId={}, type={}, action={}, requestId={}",
                userId,
                frame.getType(),
                frame.getAction(),
                frame.getRequestId());

        Command command = Command.builder()
                .userId(userId)
                .commandId(IdGenerator.generateId("cmd"))
                .requestId(frame.getRequestId())
                .action(frame.getAction())
                .payload(frame.getPayload())
                .sentAt(Instant.now())
                .build();

        log.debug("command built: commandId={}, requestId={}, userId={}, action={}",
                command.getCommandId(),
                command.getRequestId(),
                command.getUserId(),
                command.getAction());
        commandDispatcherUseCase.dispatchCommand(command);

    }   
    
}
