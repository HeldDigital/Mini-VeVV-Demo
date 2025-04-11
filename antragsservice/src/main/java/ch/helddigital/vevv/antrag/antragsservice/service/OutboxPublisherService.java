// antragsservice/src/main/java/ch/helddigital/vevv/antrag/antragsservice/service/OutboxPublisherService.java
package ch.helddigital.vevv.antrag.antragsservice.service;

import ch.helddigital.vevv.antrag.antragsservice.model.OutboxEvent;
import ch.helddigital.vevv.antrag.antragsservice.repository.OutboxEventRepository;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
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
                ProducerRecord<String, String> record = new ProducerRecord<>(
                        event.getTopic(),
                        event.getKey(),
                        event.getPayload()
                );

                CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(record);

                future.thenAccept(result -> {
                    log.info("✅ Kafka Event sent: {}", event.getId());
                    event.setSent(true);
                    outboxRepo.save(event);
                }).exceptionally(ex -> {
                    log.error("❌ Kafka Send failed for event {}: {}", event.getId(), ex.getMessage());
                    return null;
                });

            } catch (Exception e) {
                log.error("❌ Unexpected error in OutboxPublisher", e);
            }
        }
    }
}
