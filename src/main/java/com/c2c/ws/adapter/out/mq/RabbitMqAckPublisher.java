package com.c2c.ws.adapter.out.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.c2c.ws.adapter.out.mq.dto.AckDto;
import com.c2c.ws.application.model.Ack;
import com.c2c.ws.application.port.out.mq.PublishAckPort;
import com.c2c.ws.common.util.TimeFormat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RabbitMqAckPublisher implements PublishAckPort {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public RabbitMqAckPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${c2c.mq.ack.exchange}") String exchange,
            @Value("${c2c.mq.ack.routing-key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }
    @Override
    public void publishAck(Ack ack) {
        log.debug("ack = {}", ack);
        AckDto ackDto = AckDto.builder()
                .ackId(ack.getAckId())
                .eventId(ack.getEventId())
                .sentAt(TimeFormat.format(ack.getSentAt()))
                .build();
        log.debug("publishAck: exchange={}, routingKey={}, ackId={}, eventId={}",
                exchange,
                routingKey,
                ack.getAckId(),
                ack.getEventId());
        rabbitTemplate.convertAndSend(exchange, routingKey, ackDto);

    }
}
