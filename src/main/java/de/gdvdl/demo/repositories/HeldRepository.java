package de.gdvdl.demo.repositories;

import de.gdvdl.demo.domain.Held;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface HeldRepository extends JpaRepository<Held, UUID> {

    List<Held> findByName(String name);

    @Query("select a from Held a where a.name = ?1")
    List<Held> myQuery(String name);
}
