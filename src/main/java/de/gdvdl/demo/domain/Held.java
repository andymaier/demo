package de.gdvdl.demo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Held {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private double gewicht;

    @ManyToMany
    private Set<Faehigkeit> faehigkeiten;

    public Held(String name, double gewicht, Faehigkeit... faehigkeiten) {
        this.name = name;
        this.gewicht = gewicht;
        this.faehigkeiten = new HashSet<>(Arrays.asList(faehigkeiten));
    }
}
