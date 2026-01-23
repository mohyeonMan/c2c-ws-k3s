package com.c2c.ws.application.port.in.ws.command;

import com.c2c.ws.application.model.Action;
import com.c2c.ws.application.model.Command;

public interface CommandHandler {
    
    Action supports();

    void handle(Command command);

}
