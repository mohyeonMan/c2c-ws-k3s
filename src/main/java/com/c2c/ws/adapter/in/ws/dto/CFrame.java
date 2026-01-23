package com.c2c.ws.adapter.in.ws.dto;

import com.c2c.ws.application.model.Action;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CFrame {
    
    private String requestId;
    private String frameId;
    private String eventId;
    private CFrameType type;
    private Action action;
    private String payload;

}
