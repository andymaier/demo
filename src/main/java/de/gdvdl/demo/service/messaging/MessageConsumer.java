package de.gdvdl.demo.service.messaging;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageConsumer {

    //@JmsListener(destination = "my-queue")
    public void receive(JsonNode message) {
        log.info("Received message: {} ", message);
    }
}
