package de.gdvdl.demo.api;

import de.gdvdl.demo.domain.Faehigkeit;
import de.gdvdl.demo.domain.Held;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("customers")
public class CustomerController {

    private final ConcurrentHashMap<UUID, Customer> customers;

    CustomerController(ConcurrentHashMap<UUID, Customer> customers) {
        this.customers = customers;
    }

    @GetMapping
    public Collection<Customer> getCustomers() {
        return customers.values();
    }

    @GetMapping("{id}")
    public Customer getCustomer(@PathVariable UUID id) {
        return customers.get(id);
    }

    @PostMapping
    public Customer createCustomer(@RequestBody Customer customer) {
        customers.put(customer.id(), customer);
        return customers.get(customer.id());
    }

    public record Customer(UUID id, String name,
            String forname,
            int age){
    }
}
