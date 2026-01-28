package com.c2c.ws.adapter.in.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.c2c.ws.adapter.in.mq.dto.EventDto;
import com.c2c.ws.application.model.Action;
import com.c2c.ws.application.model.Event;
import com.c2c.ws.application.model.EventType;
import com.c2c.ws.application.model.Status;
import com.c2c.ws.application.port.in.mq.ConsumeEventPort;
import com.c2c.ws.application.port.in.mq.EventHandler;
import com.c2c.ws.common.util.TimeFormat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMqEventListener implements ConsumeEventPort {
    private final EventHandler eventHandler;

    @Override
    @RabbitListener(queues = "${ws.mq.event.queue}")
    public void onEvent(EventDto eventDto) {
        if (eventDto == null) {
            log.warn("event is null");
            return;
        }

        log.info("event received: requestId={}, commandId={}, eventId={}, userId={}, type={}, action={}, status={}",
                eventDto.getRequestId(),
                eventDto.getCommandId(),
                eventDto.getEventId(),
                eventDto.getUserId(),
                eventDto.getType(),
                eventDto.getAction(),
                eventDto.getStatus());

        EventType type = EventType.from(eventDto.getType());
        Status status = Status.from(eventDto.getStatus());
        Action action = Action.from(eventDto.getAction());

        Event event = Event.builder()
                .requestId(eventDto.getRequestId())
                .commandId(eventDto.getCommandId())
                .userId(eventDto.getUserId())
                .eventId(eventDto.getEventId())
                .type(type)
                .action(action)
                .status(status)
                .payload(eventDto.getPayload())
                .sentAt(TimeFormat.parse(eventDto.getSentAt()))
                .build();
        log.debug("event mapped: type={}, action={}, status={}", type, action, status);
        eventHandler.handle(event);
    }

}
