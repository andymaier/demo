package de.gdvdl.demo.api;

import de.gdvdl.demo.service.Greeter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {

    private final Greeter greeter;

    public HelloWorld(Greeter greeter) {
        this.greeter = greeter;
    }

    @RequestMapping("/hello")
    public String hello() {
        return String.format(greeter.getGreeting());
    }
}
