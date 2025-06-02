package de.gdvdl.demo.helden.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled
public class AppConfigurationTest {

    private AppConfiguration appConfiguration;

    @BeforeEach
    public void setUp() {
        appConfiguration = new AppConfiguration();
    }

    @Test
    public void testAppConfiguration() {


    }

}
