package com.c2c.ws.application.service.command;

import org.springframework.stereotype.Service;

import com.c2c.ws.application.model.Command;
import com.c2c.ws.application.port.in.ws.command.CommandHandler;
import com.c2c.ws.common.exception.C2cException;
import com.c2c.ws.common.exception.ErrorCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FallbackCommandHandler implements CommandHandler{
    
    @Override
    public void handle(Command command) {
        log.warn("unsupported action: commandId={}, requestId={}, userId={}, action={}",
                command.getCommandId(),
                command.getRequestId(),
                command.getUserId(),
                command.getAction());
        throw new C2cException(ErrorCode.WS_UNSUPPORTED_ACTION);
    }

}
