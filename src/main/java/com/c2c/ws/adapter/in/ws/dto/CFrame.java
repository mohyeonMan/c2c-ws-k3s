package com.c2c.ws.adapter.in.ws.dto;

import com.c2c.ws.application.model.Action;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CFrame {
    
    private String requestId;
    private String eventId;
    private CFrameType type;
    private Action action;
    private String payload;

}
