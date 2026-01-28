package com.c2c.ws.application.service.command;


import org.springframework.stereotype.Service;

import com.c2c.ws.application.model.Command;
import com.c2c.ws.application.port.in.ws.command.CommandHandler;
import com.c2c.ws.application.port.in.ws.session.SessionLifecycleUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@RequiredArgsConstructor
public class HeartbeatCommandHandler implements CommandHandler{
    private final SessionLifecycleUseCase sessionLifecycleUseCase;
    
    // @Override
    // public Action supports() {
    //     return Action.HEARTBEAT;
    // }

    public void handle(Command command) {
        String userId = command.getUserId();
        sessionLifecycleUseCase.touch(userId);
    }

}
