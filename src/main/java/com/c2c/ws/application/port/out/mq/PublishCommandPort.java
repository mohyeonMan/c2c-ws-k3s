package com.c2c.ws.application.port.out.mq;

import com.c2c.ws.application.model.Command;

public interface PublishCommandPort {
    void publishCommand(Command payload);
}
