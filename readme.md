# Mini-VeVV-Demo

Ein vollständiges Event-Driven-Microservice-Demo-Projekt zur Umsetzung elektronischer Anträge im Verwaltungsverfahren – inspiriert von der VeVV-Verordnung (Kanton Zürich).

## ✅ Ziel des Projekts
Diese Demo simuliert eine vollständige digitale Verfahrensabwicklung mit:
- Angular-Frontend zur Antragstellung
- Spring Boot Backend mit Kafka zur Verarbeitung
- Kafka Consumers für Archivierung und Benachrichtigung
- PostgreSQL zur Persistenz
- Kafka UI zur Einsicht der Topics
- Docker Compose für lokale Entwicklungsumgebung

Das Projekt ist **lasttestfähig, fehlertolerant** und demonstriert zentrale Konzepte wie **Retry-Mechanismen**, **Transaktionen**, **Fallbacks**, **State Management** und **resiliente Microservices**.

---

## 🚀 Skalierter Tech-Plan: VeVV Demo – Advanced Edition (5 intensive Tage)
**Fokus auf Java/Kafka Backend-Exzellenz mit realistischer Komplexität**

### 🧠 Tag 1 – Kafka-Architektur & Resilienz
- ✔️ Microservices: Antragseinreichung, Benachrichtigung, Archivierung
- ✔️ Kafka + Kafka UI mit Docker Compose
- ✔️ Erweiterung der Topic-Struktur:
  - `antraege`, `antraege-validiert`, `antraege-abgelehnt`
  - `archiv-fehler`, `dead-letter`
- Kafka Retry-Mechanismen:
  - Spring Retry (`@Retryable`, `@Recover`)
  - Manuelles Retry mit DLQ (Dead Letter Topic)
- Resilienter Consumer: Timeout, Retry, Logging
- Tests mit fehlerhaften JSON-Nachrichten

### 💼 Tag 2 – Transaktionen, Fehlerhandling, Validierung
- Spring `@Transactional` + DB-Kafka-Konsistenz
- Outbox-Pattern für Eventual Consistency
- Bean Validation (`@NotBlank`, `@Size`, etc.)
- Globales Fehlerhandling mit `@ControllerAdvice`
  - z. B. `MethodArgumentNotValidException`, `KafkaSendException`
- OpenAPI/Swagger mit Fehlerdokumentation (`400`, `409`, `500`)

### 🏋️ Tag 3 – Lasttest & Kafka Simulation
- Client-Simulator (Java CLI):
  - Multi-Threaded `POST /antrag` mit Zufallsdaten (Faker-Lib)
  - Performance-Metriken: RPS, Fehlerquote
- Kafka Benchmarking: Anträge pro Minute
- Archiv-Service künstlich verlangsamen → Backpressure sichtbar
- Logging + Alerting bei Fehlern (z. B. `archiv-fehler` Topic)

### 🔐 Tag 4 – Sicherheit & Zustellplattform
- Spring Security: geschützter Zugang zu `/api/antrag`
  - z. B. fester Bearbeiter-Token
- Dummy-Signatur-Service (PDF Sign Simulation)
- Upload-API vorbereiten (Struktur ohne echte Dateiannahme)
- API-Design: Trennung intern/extern (`/api/public`, `/api/internal`)

### 🧾 Tag 5 – Skalierbarkeit, SAGA & Präsentation
- SAGA Pattern (Archiv + Benachrichtigung müssen erfolgreich sein)
- Business-Logik: Antrag-Ablehnung (z. B. unvollständig)
- Kafka Scaling: Partitionierung, Consumer Groups
- Crash Recovery mit manuellen Service-Ausfällen getestet
- Angular-Frontend:
  - Paging, Range-Filter, max. Page Size
  - Zählung & Warnung bei zu vielen Treffern
- Finalisierung: Architekturdiagramm + Screencast (optional)

---

## 🧠 Ursprünglicher Tagesplan (Umgesetzt innerhalb von 4h)

### 🗓️ **Tag 1 – Architektur-Grundgerüst & Kafka-Lifecycle**
- ✔️ Setup der Projektstruktur (3 Microservices + 1 Frontend)
- ✔️ Kafka, Zookeeper & PostgreSQL via Docker Compose
- ✔️ Spring Boot Producer + Consumer (Hello World mit Kafka)
- ✔️ Kafka UI für Topic Monitoring
- ✔️ Angular CLI Projekt + Grundstruktur

### 🗓️ **Tag 2 – REST + Antrag-Erfassung mit Angular**
- ✔️ REST-Endpoint `POST /api/antrag` mit Spring Boot
- ✔️ Speichern des Antrags in PostgreSQL
- ✔️ Angular-Formular für Antragstellung
- ✔️ Kafka-Producer wird durch POST ausgelöst
- ✔️ End-to-End-Demo: Angular → REST → Kafka

### 🗓️ **Tag 3 – Kafka Consumer, Verarbeitung & Archivierung**
- ✔️ Service 2: Kafka-Consumer „Benachrichtigung“ → Log-Ausgabe
- ✔️ Service 3: Kafka-Consumer „Archivierung“ → PDF-Dummy + Speicherung
- ✔️ Einführung in Consumer Groups, Offsets, Partitionen
- ✔️ Fehlerbehandlung + Retry beim Kafka-Consumer
- ✔️ Crash Recovery: Consumer/Producer neustarten ohne Datenverlust

### 🗓️ **Tag 4 – API Design, Doku & Sicherheit**
- ✔️ Erweiterte REST-Fehlerbehandlung mit Status-Codes & Validation
- ✔️ Swagger/OpenAPI Dokumentation
- ✔️ CORS für Angular freigeben
- ✔️ Simpler Healthcheck-Endpoint + Error Logging Middleware
- ✔️ Konfiguration über `application.yaml` ausgelagert (zukunftsfähig)

### 🗓️ **Tag 5 – Lasttest, Resilienz, Präsentation**
- ✔️ Lasttest-Simulator: bis zu 500 parallele Clients
- ✔️ Simulierter Kafka-Ausfall + automatische Recovery
- ✔️ Retry-Strategien (Spring Retry + Kafka Consumer Backoff)
- ✔️ Paging + Range-Filter im Frontend (Angular)
- ✔️ Fehlerfeedback im UI bei zu großer Datenmenge (limit-basierte API)
- ✔️ Fallback-Mechanismus falls Archivierung/Benachrichtigung fehlschlägt
- ✔️ `README.md` mit Screenshots & Architekturdiagramm

---

## 🚀 Starten
### Voraussetzungen:
- Docker Desktop (inkl. Docker Compose)
- Java 21 (für Spring Boot)
- Node.js + Angular CLI (für das Frontend)

### Backend starten
```bash
cd docker
docker-compose up -d
```

Starte anschließend jeden Spring Boot Microservice einzeln:
```bash
cd antragsservice
./mvnw spring-boot:run

cd benachrichtigungsservice
./mvnw spring-boot:run

cd archivierungsservice
./mvnw spring-boot:run
```

### Frontend starten (Angular)
```bash
cd frontend
npm install
ng serve
```

Angular läuft auf: [http://localhost:4200](http://localhost:4200)

Kafka UI: [http://localhost:8088](http://localhost:8088) (für Debugging)

---

## 🧪 Testszenarien & Fehler-Simulation

- 🔁 **Crash Recovery:** Stoppe `docker-kafka-1`, produziere Anträge weiter → Consumer holen nach Reconnect alle Daten nach
- 🧱 **Archiv-Service stoppen:** → Teste Fallback & Logs im Benachrichtigungsservice
- 📈 **Lasttest:** Bot-Script mit 500 Requests/s → Kafka + DB Performance
- ❌ **Fehlermeldung bei kaputten POST-Daten** → validierte DTOs + Status 400
- 📄 **Millionen-Dokumente-Test:** API liefert max. 500 Einträge je Page + Warnung bei >10k
- 🔄 **Simulierter Service-Ausfall (z. B. Archiv)**: System fängt sich nach Neustart wieder automatisch

---

## 🔐 Architekturprinzipien
- **Microservice Design** mit loser Kopplung via Kafka
- **Fehlertoleranz:** Retry + Dead Letter Topics möglich
- **Skalierbarkeit:** Consumer parallelisiert via GroupId + Partition
- **Trennung von Concern:** UI, API, Verarbeitung, Archivierung getrennt
- **Transaktionssicherheit:** Outbox + Retry Logik

---

## ✨ Optional & Zukunft (nicht umgesetzt, aber vorgesehen)
- ✉️ E-Mail-Benachrichtigung (SMTP / Kafka Consumer)
- 🧾 Integration mit ELO / DMS-Schnittstelle
- 🔐 JWT-Login & SSO über Zürikonto / Keycloak
- 📦 Deployment mit GitHub Actions & Docker Registry
- 📊 Monitoring mit Prometheus & Grafana
- 🧪 Testcontainers für CI/CD & Contract Testing

---

### wichtige commands:
docker exec -it docker-kafka-1 kafka-topics --create --topic antraege-validiert --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1



## 👨‍💻 Entwickelt von [@HeldDigital](https://github.com/HeldDigital)
Für Schulungszwecke & zur Vorbereitung auf das VeVV-Projekt der Stadt Zürich



Tag 2:

💡 Was passiert hier eigentlich?
Wir setzen das Outbox-Pattern um – ein bewährtes Architekturprinzip zur eventuellen Konsistenz zwischen DB und Kafka.

🔁 Hintergrundproblem:
Stell dir vor:

Ein Benutzer sendet einen Antrag → du speicherst ihn in der Datenbank (antragRepository.save(antrag)).

Danach willst du den Antrag gleichzeitig an Kafka senden (kafkaTemplate.send(...)).

Problem:
Was, wenn der Antrag gespeichert wird, aber dann der Kafka-Versand fehlschlägt (Crash, Netzwerk, Fehler)?

→ Inkonistenz! Die Datenbank enthält den Antrag, aber die Event-Consumer wissen nichts davon.

✅ Lösung: Outbox Pattern
Wir machen’s besser:

Antrag speichern + Kafka-Nachricht als OutboxEvent speichern → in 1 Transaktion.

Später (asynchron) prüft ein Scheduler alle OutboxEvent mit sent = false.

Für jedes Event:

Wir senden es an Kafka

Wenn erfolgreich → sent = true in der DB → ✅ erledigt

Vorteil:
Keine Nachricht geht verloren

Kafka wird nur benachrichtigt, wenn auch die DB gespeichert wurde

Crash-sicher: Bei Neustart läuft der Scheduler einfach weiter


Siehe : `docs/dev-notes-outbox.md` 

