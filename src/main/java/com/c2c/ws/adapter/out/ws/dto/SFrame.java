package com.c2c.ws.adapter.out.ws.dto;

import com.c2c.ws.application.model.Action;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class SFrame {

    private String requestId;
    private String frameId;
    private String eventId;
    private SFrameType type;
    private Action action;
    private String payload;
    private Status status;
    
}
