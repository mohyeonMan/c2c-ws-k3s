package com.c2c.ws.application.port.in.ws.command;

import com.c2c.ws.application.model.Command;

public interface CommandDispatcherUseCase {
    
    void dispatchCommand(Command command);

}
