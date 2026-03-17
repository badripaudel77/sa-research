package com.example.productservice.messaging;

import com.example.productservice.event.OrderCreatedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String orderCreatedTopic;

    public OrderEventPublisher(KafkaTemplate<String, String> kafkaTemplate,
        ObjectMapper objectMapper,
        @Value("${app.kafka.order-created-topic}") String orderCreatedTopic) {
                this.kafkaTemplate = kafkaTemplate;
                this.objectMapper = objectMapper;
                this.orderCreatedTopic = orderCreatedTopic;
    }

    public void publish(OrderCreatedEvent event) {
        try {
            String payload = objectMapper.writeValueAsString(event);
            kafkaTemplate.send(orderCreatedTopic, event.productNumber(), payload);
        } 
        catch (Exception ex) {
            LOGGER.warn("Failed to publish order event for productNumber={}", event.productNumber(), ex);
        }
    }
}
