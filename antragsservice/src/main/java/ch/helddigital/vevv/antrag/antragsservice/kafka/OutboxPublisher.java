// antragsservice/src/main/java/ch/helddigital/vevv/antrag/antragsservice/kafka/OutboxPublisher.java
package ch.helddigital.vevv.antrag.antragsservice.kafka;

import ch.helddigital.vevv.antrag.antragsservice.model.OutboxEvent;
import ch.helddigital.vevv.antrag.antragsservice.repository.OutboxEventRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OutboxPublisher {

    private final OutboxEventRepository repository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxPublisher(OutboxEventRepository repository,
                           KafkaTemplate<String, String> kafkaTemplate) {
        this.repository = repository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedRate = 10000) // alle 10 Sekunden
    public void publishPendingEvents() {
        List<OutboxEvent> events = repository.findTop50BySentFalseOrderByCreatedAtAsc();
        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(event.getTopic(), event.getKey(), event.getPayload()).get();
                event.setSent(true);
                repository.save(event);
            } catch (Exception e) {
                System.err.println("❌ Kafka-Fehler: " + e.getMessage());
            }
        }
    }
}
