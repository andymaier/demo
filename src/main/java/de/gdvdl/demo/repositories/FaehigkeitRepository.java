package de.gdvdl.demo.repositories;

import de.gdvdl.demo.domain.Faehigkeit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FaehigkeitRepository  extends JpaRepository<Faehigkeit, UUID> {
    Faehigkeit findByName(String name);
}
