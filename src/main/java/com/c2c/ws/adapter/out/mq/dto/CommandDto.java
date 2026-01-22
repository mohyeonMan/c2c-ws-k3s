package com.c2c.ws.adapter.out.mq.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommandDto {
    private String commandId;
    private String requestId;
    private String userId; 
    private String action;
    private String payload;
    private String sentAt;
}
