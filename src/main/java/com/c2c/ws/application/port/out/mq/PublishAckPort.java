package com.c2c.ws.application.port.out.mq;

import com.c2c.ws.application.model.Ack;

public interface PublishAckPort {

    public void publishAck(Ack ack);
}
