package de.gdvdl.demo.repositories;

import de.gdvdl.demo.domain.Faehigkeit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FaehigkeitRepository  extends JpaRepository<Faehigkeit, UUID> {
}
