package de.gdvdl.demo.service.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageProducer {

    private final JmsTemplate jmsTemplate;

    public MessageProducer(JmsTemplate template) {
        this.jmsTemplate = template;
    }

    public void send(String destination, String message) {
        jmsTemplate.convertAndSend(destination, message);
    }
}
