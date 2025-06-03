package de.gdvdl.demo.api;

import de.gdvdl.demo.domain.Held;
import de.gdvdl.demo.repositories.HeldRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("helden")
public class HeldenController {

    private final HeldRepository repo;

    public HeldenController(HeldRepository heldRepository) {
        this.repo = heldRepository;
    }

    @GetMapping
    public List<Held> index() {
        return repo.findAll();
    }

    @GetMapping("{id}")
    public Held getById(@PathVariable UUID id) {
        return repo.findById(id).get();
    }



}
