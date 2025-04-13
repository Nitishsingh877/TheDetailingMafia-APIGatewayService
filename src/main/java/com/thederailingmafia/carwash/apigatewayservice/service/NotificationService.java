package com.thederailingmafia.carwash.apigatewayservice.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class NotificationService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "notifications.queue")
    public void receiveMessage(String message) {
        try {
            Map<String, Object> event = objectMapper.readValue(message, Map.class);
            String eventType = (String) event.get("event");
            switch (eventType) {
                case "order.created":
                    System.out.println("Notification: Order " + event.get("orderId") + " created for " + event.get("washerEmail"));
                    break;
                case "order.updated":
                    System.out.println("Notification: Order " + event.get("orderId") + " status updated to " + event.get("status") + " for " + event.get("washerEmail"));
                    break;
                case "payment.confirmed":
                    System.out.println("Notification: Payment " + event.get("paymentId") + " confirmed for order " + event.get("orderId"));
                    break;
                default:
                    System.out.println("Unknown event: " + eventType);
            }
        } catch (Exception e) {
            System.err.println("Error processing notification: " + e.getMessage());
        }
    }
}
