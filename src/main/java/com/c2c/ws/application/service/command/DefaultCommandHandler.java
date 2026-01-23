package com.c2c.ws.application.service.command;

import org.springframework.stereotype.Service;

import com.c2c.ws.application.model.Command;
import com.c2c.ws.application.port.in.ws.command.CommandHandler;
import com.c2c.ws.application.port.out.mq.PublishCommandPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultCommandHandler implements CommandHandler{
    private final PublishCommandPort publishCommandPort;

    public void handle(Command command) {
        
        log.info("hadle defaultCommand :: command = {}", command);
        publishCommandPort.publishCommand(command);
        
    }

}
