package com.c2c.ws.application.model;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Ack {

    private final String ackId;
    private final String eventId;
    private final Action action; 
    private final Instant sentAt;
    
}
