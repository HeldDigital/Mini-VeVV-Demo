Klar! Hier ist die komplette `README_DevNotes.md` – copy & paste ready für dein Projekt:
# 🛠️ Dev Notes – Mini-VeVV Advanced

Diese Datei dient als technische Entwickler-Zusammenfassung für das Kafka-basierte Microservice-Demo-Projekt mit Angular-Frontend. Ziel ist eine realitätsnahe, fehlertolerante Architektur wie sie im VeVV-Projekt (Stadt Zürich) zum Einsatz kommen könnte.

---

## 🎯 Zielbild

Die Demo bildet ein modernes, eventbasiertes Behördenverfahren ab:
- Microservices mit resilientem Kafka-Messaging
- Verteilte Verarbeitung (Archiv + Benachrichtigung)
- Performance- und Lasttestbar
- Frontend mit State Management und Paging

---

## 🔧 Geplante & Umgesetzte Features (Backend & Frontend)

### ✅ 1. Outbox Pattern (Eventual Consistency)
- Nachrichten werden erst persistiert, dann über Outbox-Publisher asynchron an Kafka gesendet.
- Garantie: Kafka-Publish nur bei erfolgreicher Transaktion (kein „lost update“ bei Crashs)

### ✅ 2. DLQ + Retry-Strategie
- Fehlerhafte Kafka-Nachrichten landen in Dead Letter Topics.
- Retry über REST/CLI: z. B. `/api/admin/retry/archiv/123`
- Ziel: Fehlerbehandlung nicht automatisch → kontrolliert & nachvollziehbar

### ✅ 4. Lasttest-Client (Java CLI Tool)
- Simuliert hunderte gleichzeitige POST-Requests mit Faker-Daten.
- Metriken: Antwortzeit, Erfolgsquote, Kafka-Lag
- Grundlage für Benchmarks & Performance-Tuning

### ✅ 5. SAGA Pattern
- Wenn Archivierung fehlschlägt, wird Benachrichtigung unterdrückt oder Fehler gemeldet.
- Kompensationslogik möglich, z. B. Antrag auf „zurückgestellt“ setzen

### ✅ 6. Kafka Logging in PostgreSQL
- Jede Kafka-Nachricht (Inbound/Outbound) wird optional persistiert
- Felder: `timestamp`, `topic`, `status`, `payload`, `traceId`
- Ideal für Debugging und Monitoring

### ✅ 7. Resilience & Service Fallbacks
- Fallbacks bei Ausfall des Archivierungsservices
- Einsatz von Resilience4j für:
  - Retry mit Delay
  - Circuit Breaker bei Dauerfehlern
  - Logging/Alerting bei Eskalation

---

## 🔐 Transaktionen & Fehlerbehandlung

- Spring `@Transactional` für atomare Datenbankoperationen
- Globale Exception-Handler via `@ControllerAdvice`
- Swagger/OpenAPI enthält 400/409/500 Fehlerbeschreibungen
- Retry bei Kafka über Spring Retry und manuelle DLQ-Strategie

---

## 🔄 Verteilte Microservices

| Service              | Rolle                            | Besonderheit             |
|----------------------|----------------------------------|--------------------------|
| AntragService        | REST + Kafka Producer            | Port fix, nur 1 Instanz  |
| ArchivierungsService | Kafka Consumer + DB Speicherung | Mehrfach startbar        |
| BenachrichtigungsSvc | Kafka Consumer + Fallback        | Mehrfach startbar        |

---

## 📊 Angular Frontend

- **Einfach gehalten, aber professionell:**
  - Anzeige eingereichter Anträge
  - Status-Farben (grün = archiviert, rot = Fehler)
- **State Management:**
  - Zentrale Services mit `BehaviorSubject`
  - Fehlerstatus/Verarbeitung zentral gepflegt
- **Paging & Performance:**
  - Nur limitierter Abruf (`/api/antrag?page=1&limit=100`)
  - Warnung bei zu großem Datenabruf (z. B. 10.000+)
  - Query-Filter (z. B. nach Zeitstempel oder Status)

---

## 📁 Verzeichnisse

```bash
/cmd              → Start-/Stop-Skripte für Windows 11 (Docker + Java)
/docker           → docker-compose + persistente Kafka/PostgreSQL Volumes
/antragsservice   → Spring Boot App mit REST + Kafka Producer
/archivservice    → Spring Boot App mit Kafka Consumer + DB
/benachrichtigung → Spring Boot App mit Kafka Consumer
/frontend         → Angular App (Formulare + Anzeige)
/simulator        → Lasttest-Client (Java CLI)
```

---

## 🧠 Optional & Nice to Have (in Planung)

- PDF-Erstellung & Fake-Signatur-Endpoint
- Automatische Tests (JUnit + Testcontainers)
- Prometheus + Grafana für Monitoring
- Upload-Logik mit Dummy-Dateien
- Admin-Dashboard für DLQ-Retrys & Topic-Status

---

## ✅ Start-Tipps (Windows 11)

```bash
cd docker\cmd
.\start-all.cmd       # startet Kafka + PostgreSQL + alle Services
.\stop-all.cmd        # beendet alles sauber
```

---

## 📣 Hinweis

Dieses Projekt wurde bewusst als realistisches Test- & Lernsystem konzipiert. Es eignet sich zur Demonstration echter Enterprise-Patterns, zur Interviewvorbereitung und als Grundlage für Behördendigitalisierung mit Kafka & Java.

---

👨‍💻 Maintainer: [@HeldDigital](https://github.com/HeldDigital)
