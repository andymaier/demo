package de.gdvdl.demo.api;

import de.gdvdl.demo.domain.Held;
import de.gdvdl.demo.repositories.HeldRepository;
import de.gdvdl.demo.repositories.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("helden")
@Slf4j
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
        return repo.findById(id).orElseThrow(NotFoundException::new);
    }

    @GetMapping("search")
    public Held getByString(@RequestParam String name) {
        log.info(name);
        return repo.findByName(name).stream().findFirst().orElseThrow(NotFoundException::new);
    }
}
