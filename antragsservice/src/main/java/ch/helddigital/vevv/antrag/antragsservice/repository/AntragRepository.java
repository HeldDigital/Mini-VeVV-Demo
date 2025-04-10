package ch.helddigital.vevv.antrag.antragsservice.repository;

import ch.helddigital.vevv.antrag.antragsservice.model.Antrag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AntragRepository extends JpaRepository<Antrag, Long> {
}
