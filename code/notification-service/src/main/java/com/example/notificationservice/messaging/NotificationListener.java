package com.example.notificationservice.messaging;

import com.example.notificationservice.event.OrderCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationListener.class);

    private final ObjectMapper objectMapper;

    public NotificationListener(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${app.kafka.order-created-topic}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(@Payload String payload) {
        try {
            OrderCreatedEvent event = objectMapper.readValue(payload, OrderCreatedEvent.class);
            LOGGER.info("Received event: {}", event);
        } 
        catch (Exception e) {
            LOGGER.error("Failed to deserialize event payload: {}", payload, e);
        }
    }
}
