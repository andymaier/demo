package de.gdvdl.demo.service.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.gdvdl.demo.domain.Held;
import de.gdvdl.demo.repositories.HeldRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageConsumer {

    private final HeldRepository repository;
    private final ObjectMapper mapper;

    public MessageConsumer(HeldRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @JmsListener(destination = "my-queue")
    public void receive(String message) throws JsonProcessingException {
        log.info("Received message: {} ", message);
        Held held = mapper.readValue(message, Held.class);
        repository.save(held);
    }
}
