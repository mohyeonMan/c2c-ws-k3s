package com.c2c.ws.application.service.command;

import org.springframework.stereotype.Service;

import com.c2c.ws.application.model.Action;
import com.c2c.ws.application.model.Command;
import com.c2c.ws.application.port.in.ws.command.CommandDispatcherUseCase;
import com.c2c.ws.application.port.in.ws.command.CommandHandler;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommandDispatcher implements CommandDispatcherUseCase{
    private final HeartbeatCommandHandler heartbeatCommandHandler;
    private final DefaultCommandHandler defaultCommandHandler;
    private final FallbackCommandHandler fallbackCommandHandler;

    @Override
    public void dispatchCommand(Command command) {
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

        handler.handle(command);
    }

}
