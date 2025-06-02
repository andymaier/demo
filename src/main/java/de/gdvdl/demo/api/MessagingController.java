package de.gdvdl.demo.api;

import de.gdvdl.demo.service.messaging.MessageProducer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("messaging")
public class MessagingController {

    private final MessageProducer messageProducer;

    public MessagingController(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @PostMapping
    public void sendMessage(@RequestBody Msg message) {
        messageProducer.send(message.destination(), message.message());
    }

    public record Msg(String destination, String message) {}

}
