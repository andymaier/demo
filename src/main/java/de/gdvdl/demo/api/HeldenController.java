package de.gdvdl.demo.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.gdvdl.demo.domain.Faehigkeit;
import de.gdvdl.demo.domain.Held;
import de.gdvdl.demo.repositories.FaehigkeitRepository;
import de.gdvdl.demo.repositories.HeldRepository;
import de.gdvdl.demo.repositories.NotFoundException;
import de.gdvdl.demo.service.messaging.MessageProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("helden")
@Slf4j
public class HeldenController {

    private final HeldRepository repo;
    private final FaehigkeitRepository faehigkeitRepository;
    private final MessageProducer producer;
    private final ObjectMapper mapper;

    public HeldenController(HeldRepository heldRepository,
                            FaehigkeitRepository faehigkeitRepository,
                            MessageProducer producer,
                            ObjectMapper mapper) {
        this.repo = heldRepository;
        this.faehigkeitRepository = faehigkeitRepository;
        this.producer = producer;
        this.mapper = mapper;
    }

    @GetMapping
    public List<Held> index() {
        log.info("Helden wurden abgerufen");
        return repo.findAll();
    }

    @GetMapping("{id}")
    public Held getById(@PathVariable UUID id) {
        log.info("Held wurde abgerufen" + id);
        return repo.findById(id).orElseThrow(NotFoundException::new);
    }

    @GetMapping("search")
    public Held getByString(@RequestParam String name) {
        log.info(name);
        log.info("Held wird gesucht: " + name);
        return repo.findByName(name).stream().findFirst().orElseThrow(NotFoundException::new);
    }

    /*
    @PostMapping
    public ResponseEntity<Held> create(@RequestBody Held held, UriComponentsBuilder builder) {
        log.info(held.toString());

        held.getFaehigkeiten().stream().forEach(x -> {
            faehigkeitRepository.save(x);
        });

        boolean faehigkeitAvailable = held.getFaehigkeiten().stream().allMatch(x -> {
            return faehigkeitRepository.findByName(x.getName()).isEmpty();
        });

        Held newHeld = repo.save(held);
        URI uri = builder.path("/helden/{id}").buildAndExpand(newHeld.getId()).toUri();
        return ResponseEntity.created(uri).body(newHeld);
    }*/

    @PostMapping
    public ResponseEntity<Held> create(@RequestBody Held held, UriComponentsBuilder builder) throws JsonProcessingException {
        UUID uuid = UUID.randomUUID();
        held.setId(uuid);
        held.getFaehigkeiten().forEach(this::findOrCreateFaehigkeiten);
        producer.send("my-queue", mapper.writeValueAsString(held));
        //Held newHeld = repo.save(held);
        ///URI uri = builder.path("/helden/{id}").buildAndExpand(newHeld.getId()).toUri();
        return ResponseEntity.accepted().build();
    }

    private void findOrCreateFaehigkeiten(Faehigkeit faehigkeit) {
        var found = faehigkeitRepository.findByName(faehigkeit.getName());
        if (found == null) {
            faehigkeitRepository.save(faehigkeit);
        } else {
            faehigkeit.setId(found.getId());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity<Held> update(@PathVariable UUID id, @RequestBody Held updatedHeld) {
        log.info("Held wird geändert: " + updatedHeld.toString());
        Held existingHeld = repo.findById(id).orElseThrow(NotFoundException::new);

        existingHeld.setName(updatedHeld.getName());
        existingHeld.setGewicht(updatedHeld.getGewicht());

        existingHeld.getFaehigkeiten().clear();
        if (updatedHeld.getFaehigkeiten() != null) {
            updatedHeld.getFaehigkeiten().forEach(faehigkeit -> findOrCreateFaehigkeiten(faehigkeit));
            existingHeld.getFaehigkeiten().addAll(updatedHeld.getFaehigkeiten());
        }

        // Save the updated object
        Held savedHeld = repo.save(existingHeld);
        return ResponseEntity.ok(savedHeld);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("Held wird gelöscht: " + id);
        Held held = repo.findById(id).orElseThrow(NotFoundException::new);
        repo.delete(held);
        return ResponseEntity.noContent().build();
    }

}
