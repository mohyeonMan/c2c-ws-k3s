package com.c2c.ws.application.service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.c2c.ws.application.model.Action;
import com.c2c.ws.application.model.Command;
import com.c2c.ws.application.port.in.ws.command.CommandDispatcherUseCase;
import com.c2c.ws.application.port.in.ws.command.CommandHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommandDispatcher implements CommandDispatcherUseCase{
    private final Map<Action, CommandHandler> commandHandlerMap;

    public CommandDispatcher(List<CommandHandler> handlers) {
        Objects.requireNonNull(handlers, "handlers");
        Map<Action, CommandHandler> mapped = new EnumMap<>(Action.class);
        for (CommandHandler handler : handlers) {
            if (handler == null || handler.supports() == null) {
                continue;
            }
            mapped.put(handler.supports(), handler);
        }
        this.commandHandlerMap = mapped;
    }

    @Override
    public void dispatchCommand(Command command) {
        
        

    }

}