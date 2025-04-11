// antragsservice/src/main/java/ch/helddigital/vevv/antrag/antragsservice/service/OutboxPublisherService.java
package ch.helddigital.vevv.antrag.antragsservice.service;

import ch.helddigital.vevv.antrag.antragsservice.model.OutboxEvent;
import ch.helddigital.vevv.antrag.antragsservice.repository.OutboxEventRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class OutboxPublisherService {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisherService.class);
    private final OutboxEventRepository outboxRepo;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxPublisherService(OutboxEventRepository outboxRepo, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepo = outboxRepo;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {
        List<OutboxEvent> events = outboxRepo.findTop50BySentFalseOrderByCreatedAtAsc();
        for (OutboxEvent event : events) {
            try {
                sendeMitRetry(event);
            } catch (Exception e) {
                log.error("❌ Fehler beim Senden mit Retry: {}", e.getMessage());
            }
        }
    }

    @Retryable(
        value = { RuntimeException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 2000)
    )
    public void sendeMitRetry(OutboxEvent event) {
        ProducerRecord<String, String> record = new ProducerRecord<>(event.getTopic(), event.getKey(), event.getPayload());

        CompletableFuture<?> future = kafkaTemplate.send(record)
            .thenAccept(result -> {
                log.info("✅ Kafka Event sent: {}", event.getId());
                event.setSent(true);
                outboxRepo.save(event);
            })
            .exceptionally(error -> {
                throw new RuntimeException("❌ Kafka-Versand fehlgeschlagen: " + error.getMessage(), error);
            });

        // Wichtig: damit Retry greift → blockieren bis Erfolg oder Fehler
        future.join(); 
    }

    @Recover
    public void sendeFallback(RuntimeException e, OutboxEvent event) {
        log.error("🔁 Fallback aktiv – Kafka-Versand gescheitert nach allen Retries: Event ID {}", event.getId());
        // TODO: Event optional in DLQ verschieben
    }
}
