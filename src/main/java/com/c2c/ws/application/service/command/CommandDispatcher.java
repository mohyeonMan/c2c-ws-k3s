package com.c2c.ws.application.service.command;

import org.springframework.stereotype.Service;

import com.c2c.ws.application.model.Action;
import com.c2c.ws.application.model.Command;
import com.c2c.ws.application.port.in.ws.command.CommandDispatcherUseCase;
import com.c2c.ws.application.port.in.ws.command.CommandHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommandDispatcher implements CommandDispatcherUseCase{
    private final HeartbeatCommandHandler heartbeatCommandHandler;
    private final DefaultCommandHandler defaultCommandHandler;
    private final FallbackCommandHandler fallbackCommandHandler;

    @Override
    public void dispatchCommand(Command command) {
        if (command == null) {
            log.warn("dispatchCommand: command is null");
            return;
        }

        Action action = command.getAction();
        CommandHandler handler; 

        switch (action) {
            case HEARTBEAT:
                handler = heartbeatCommandHandler;
                break;
            case UNKNOWN:
                handler = fallbackCommandHandler;
                break;
            default:
                handler = defaultCommandHandler;
                break;
        }

        log.debug("dispatchCommand: action={}, commandId={}, requestId={}, userId={}, handler={}",
                action,
                command.getCommandId(),
                command.getRequestId(),
                command.getUserId(),
                handler.getClass().getSimpleName());
        handler.handle(command);
    }

}
