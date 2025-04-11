## 💡 Outbox Pattern – Crash-sicheres Kafka Messaging

## 🧠 Zusammenfassung


               +-------------------+
               |   AntragService   |
               +--------+----------+
                        |
                        | 1. speichert Antrag + Event (in 1 Transaktion)
                        v
               +-------------------+
               |   OutboxEvent DB  |  <--- ❗persistente Zwischenablage
               +--------+----------+
                        |
            alle 5s     |
         Scheduled Task | 2. liest alle neuen Events (sent=false)
                        |
                        v
               +-------------------+
               | Kafka Publisher   |
               +--------+----------+
                        |
                        v
                 Kafka Topic (z.B. antraege)


💎 Vorteil:
- Kafka **nur nach erfolgreicher Transaktion**
- Nach Crash/Restart → Events gehen **nicht verloren**
- Später kann man den Publisher leicht als **eigenen Microservice auslagern**



### 🔁 Hintergrundproblem


```java
// antragsservice/src/main/java/ch/helddigital/vevv/antrag/antragsservice/service/AntragService.java

antragRepository.save(antrag);                   // 🟢 Antrag gespeichert in DB
kafkaTemplate.send("antraege", antrag);          // 🔴 Kafka sendet Event
```

**❌ Problem:**  
Was, wenn `send(...)` fehlschlägt (z. B. Crash, Timeout, Kafka nicht erreichbar)?  
→ **Inkonistenz**! Der Antrag ist in der Datenbank, aber **nicht** bei Kafka angekommen. Consumer wie z. B. Archivierung oder Benachrichtigung wissen nichts davon.

---

### ✅ Lösung: Outbox Pattern

#### 1. **Antrag speichern + OutboxEvent erzeugen – in 1 Transaktion**

```java
// antragsservice/src/main/java/ch/helddigital/vevv/antrag/antragsservice/service/AntragService.java

@Transactional
public void saveAntragAndQueueKafkaEvent(AntragDTO dto) throws Exception {
    Antrag antrag = antragMapper.toEntity(dto);
    antragRepository.save(antrag);

    OutboxEvent event = new OutboxEvent();
    event.setTopic("antraege");
    event.setKey(antrag.getId().toString());
    event.setPayload(objectMapper.writeValueAsString(dto));
    event.setSent(false);

    outboxEventRepository.save(event);  // 📝 Persistiert für späteren Kafka-Versand
}
```

---

#### 2. **Asynchroner Kafka-Versand via Scheduler**

```java
// antragsservice/src/main/java/ch/helddigital/vevv/antrag/antragsservice/scheduler/OutboxEventScheduler.java

@Scheduled(fixedDelay = 5000)
public void sendPendingOutboxEvents() {
    List<OutboxEvent> pendingEvents = outboxEventRepository.findTop50BySentFalseOrderByCreatedAtAsc();

    for (OutboxEvent event : pendingEvents) {
        try {
            kafkaTemplate.send(event.getTopic(), event.getKey(), event.getPayload()).get(); // ✅ synchron warten
            event.setSent(true);
            outboxEventRepository.save(event); // 🟢 Jetzt gilt's als "gesendet"
            log.info("✅ Kafka Event sent: {}", event.getKey());
        } catch (Exception ex) {
            log.error("❌ Kafka Send failed for event {}: {}", event.getKey(), ex.getMessage());
        }
    }
}
```

---

### 🧠 Warum das besser ist

| Klassisch (ohne Outbox)      | Mit Outbox Pattern             |
|------------------------------|--------------------------------|
| Kein Schutz vor Eventverlust | Kafka-Versand ist **retrybar** |
| Transaktion = DB, **aber nicht Kafka** | Transaktion nur in DB, Kafka folgt später |
| Schwer nachzuvollziehen      | Alle Events sind in der DB     |

---

### ✅ Vorteile auf einen Blick

- **Keine Nachricht geht verloren** (Crash-resistent)
- **Kafka wird nur benachrichtigt, wenn auch DB gespeichert wurde**
- **Verpasste Kafka-Nachrichten** können bei Neustart nachgeholt werden
- Später skalierbar: Auslagerung als eigener Microservice (`outbox-dispatcher`)

---