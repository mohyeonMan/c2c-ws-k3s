package com.c2c.ws.adapter.out.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.c2c.ws.adapter.out.mq.dto.CommandDto;
import com.c2c.ws.application.model.Command;
import com.c2c.ws.application.port.out.mq.PublishCommandPort;
import com.c2c.ws.common.util.TimeFormat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RabbitMqCommandPublisher implements PublishCommandPort {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public RabbitMqCommandPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${c2c.mq.command.exchange}") String exchange,
            @Value("${c2c.mq.command.routing-key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Override
    public void publishCommand(Command command) {
        log.info("command = {}", command);
        CommandDto commandDto = CommandDto.builder()
                .commandId(command.getCommandId())
                .requestId(command.getRequestId())
                .userId(command.getUserId())
                .action(command.getAction().name())
                .payload(command.getPayload())
                .sentAt(TimeFormat.format(command.getSentAt()))
                .build();

        log.debug("publishCommand: exchange={}, routingKey={}, commandId={}, requestId={}, userId={}",
                exchange,
                routingKey,
                command.getCommandId(),
                command.getRequestId(),
                command.getUserId());
        rabbitTemplate.convertAndSend(exchange, routingKey, commandDto);
    }
}
