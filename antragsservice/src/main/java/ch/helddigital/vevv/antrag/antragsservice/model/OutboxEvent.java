// antragsservice/src/main/java/ch/helddigital/vevv/antrag/antragsservice/model/OutboxEvent.java
package ch.helddigital.vevv.antrag.antragsservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "outbox_event")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;
    private String key;

    @Lob
    private String payload;

    private boolean sent;

    @Column(name = "created_at")
    private Instant createdAt;
}
