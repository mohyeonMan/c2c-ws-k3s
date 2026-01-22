package com.c2c.ws.application.model;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Command {
    private final String commandId;
    private final String requestId;
    private final String userId; 
    private final Action action;
    private final String payload;
    private final Instant sentAt;
}
