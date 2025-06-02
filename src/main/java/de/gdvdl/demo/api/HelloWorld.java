package de.gdvdl.demo.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorld {


    final String myname;

    public HelloWorld(@Qualifier("myname") final String myname) {
        this.myname = myname;
    }

    @RequestMapping("/hello")
    public String hello() {
        return String.format("Hello %s!", myname);
    }
}
