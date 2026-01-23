package com.c2c.ws.application.port.in.mq;

import com.c2c.ws.adapter.in.mq.dto.EventDto;

public interface ConsumeEventPort {
    void onEvent(EventDto eventDto);

}
