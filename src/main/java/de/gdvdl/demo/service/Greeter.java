package de.gdvdl.demo.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class Greeter {
    private final String name;

    public Greeter(@Qualifier("myname")  String name) {
        this.name = name;
    }

    public String getGreeting() {
        return "Hello " + name + "!";
    }
}
