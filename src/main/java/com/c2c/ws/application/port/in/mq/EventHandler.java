package com.c2c.ws.application.port.in.mq;

import com.c2c.ws.application.model.Event;

public interface EventHandler {
    void handle(Event event);
}
