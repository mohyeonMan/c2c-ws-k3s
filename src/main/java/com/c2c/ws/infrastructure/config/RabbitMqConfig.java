package com.c2c.ws.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Declarables commandMqDeclarables(
            @Value("${c2c.mq.command.exchange}") String exchangeName,
            @Value("${c2c.mq.command.queue}") String queueName,
            @Value("${c2c.mq.command.routing-key}") String routingKey
    ) {
        TopicExchange exchange = new TopicExchange(exchangeName, true, false);
        Queue queue = new Queue(queueName, true, false, false);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        return new Declarables(exchange, queue, binding);
    }

    @Bean
    public Declarables ackMqDeclarables(
            @Value("${c2c.mq.ack.exchange}") String exchangeName,
            @Value("${c2c.mq.ack.queue}") String queueName,
            @Value("${c2c.mq.ack.routing-key}") String routingKey
    ) {
        TopicExchange exchange = new TopicExchange(exchangeName, true, false);
        Queue queue = new Queue(queueName, true, false, false);
        Binding binding = BindingBuilder.bind(queue).to(exchange).with(routingKey);
        return new Declarables(exchange, queue, binding);
    }
}
