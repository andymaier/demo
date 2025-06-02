package de.gdvdl.demo.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld2 {

    @RequestMapping("/hello2")
    public String hello2() {
        return "Hello gdv-dl das soll nun anders heißen!";

    }
}
