package de.gdvdl.demo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Faehigkeit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String name;

    /*@ManyToMany
    @JoinTable(
            name = "held_faehigkeit",
            joinColumns = @JoinColumn(name = "faehigkeit_id"),
            inverseJoinColumns = @JoinColumn(name = "held_id")
    )
    private Set<Held> helden = new HashSet<>();
*/

}
