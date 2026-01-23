package com.c2c.ws.application.service.command;

import org.springframework.stereotype.Service;

import com.c2c.ws.application.model.Command;
import com.c2c.ws.application.port.in.ws.command.CommandHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FallbackCommandHandler implements CommandHandler{
    
    @Override
    public void handle(Command command) {
        log.info("UNSUPPORTED ACTION = {}", command.toString());
        throw new RuntimeException("UNSUPPORTED ACTION");
    }

}
