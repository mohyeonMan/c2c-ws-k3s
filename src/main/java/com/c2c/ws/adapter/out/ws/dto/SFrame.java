package com.c2c.ws.adapter.out.ws.dto;

import com.c2c.ws.application.model.Action;
import com.c2c.ws.application.model.EventType;
import com.c2c.ws.application.model.Status;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SFrame {

    private String requestId;
    private String resId;
    private String eventId;
    private EventType type;
    private Action action;
    private String payload;
    private Status status;
    
}
