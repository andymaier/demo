package de.gdvdl.demo.helden.config;

import de.gdvdl.demo.api.CustomerController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
public class AppConfiguration {

    @Bean("myname")
    public String myname() {
        return "Till";
    }

    @Bean
    public ConcurrentHashMap<UUID, CustomerController.Customer> concurrentHashMap() {
        return new ConcurrentHashMap<>();
    }
}
