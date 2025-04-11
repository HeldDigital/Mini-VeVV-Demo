// antragsservice/src/main/java/ch/helddigital/vevv/antrag/antragsservice/service/OutboxService.java
package ch.helddigital.vevv.antrag.antragsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ch.helddigital.vevv.antrag.antragsservice.model.OutboxEvent;
import ch.helddigital.vevv.antrag.antragsservice.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;

    public void saveOutboxEvent(String topic, Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            OutboxEvent event = OutboxEvent.builder()
                    .topic(topic)
                    .payload(json)
                    .createdAt(Instant.now())
                    .sent(false)
                    .build();

            outboxEventRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Serialisieren von Outbox-Payload", e);
        }
    }
}
