package de.gdvdl.demo.api;

import de.gdvdl.demo.service.Greeter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
public class HelloWorldTest {

    private HelloWorld helloWorld;

    @BeforeEach
    public void setUp() {
        Greeter greeter = new Greeter("Hello From Test");
        helloWorld = new HelloWorld(greeter);
    }

    @Test
    public void testHelloWorld() {
        String result = helloWorld.hello();
        //Assert.isTrue(result.equals("Hello Hello From Test"), "Hello From Test");
        Assertions.assertEquals("Hello Hello From Test!", result);
    }

}
