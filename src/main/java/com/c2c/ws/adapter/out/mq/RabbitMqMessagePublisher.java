package com.c2c.ws.adapter.out.mq;

import java.util.Objects;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.c2c.ws.application.port.out.PublishMessagePort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RabbitMqMessagePublisher implements PublishMessagePort {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public RabbitMqMessagePublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${c2c.mq.event.exchange:c2c.event}") String exchange,
            @Value("${c2c.mq.event.routing-key:event.event}") String routingKey) {
        this.rabbitTemplate = Objects.requireNonNull(rabbitTemplate, "rabbitTemplate");
        this.exchange = Objects.requireNonNull(exchange, "exchange");
        this.routingKey = Objects.requireNonNull(routingKey, "routingKey");
    }

    @Override
    public void publish(String payload) {
        log.info("payload = {}",payload);
        rabbitTemplate.convertAndSend(exchange, routingKey, payload);
    }
}
