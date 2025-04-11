// antragsservice/src/main/java/ch/helddigital/vevv/antrag/antragsservice/repository/OutboxEventRepository.java
package ch.helddigital.vevv.antrag.antragsservice.repository;

import ch.helddigital.vevv.antrag.antragsservice.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
    List<OutboxEvent> findTop50BySentFalseOrderByCreatedAtAsc();
}
