package com.c2c.ws.application.model;

import com.c2c.ws.adapter.out.ws.dto.SFrameType;
import com.c2c.ws.adapter.out.ws.dto.Status;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Event {
    private final String requestId;
    private final String frameId;
    private final String userId;
    private final String eventId;
    private final SFrameType type;
    private final Action action;
    private final String payload;
    private final Status status;
}
