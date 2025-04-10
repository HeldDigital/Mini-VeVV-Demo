# ✨ Mini-VeVV-Demo-Projekt

> 🏛️ Umsetzung einer vereinfachten „Verordnung über elektronische Verfahrenshandlungen im Verwaltungsverfahren (VeVV)“ mit Kafka, Angular und Microservices

## ⚡️ Ziel
Ein lauffähiges, lokal startbares Fullstack-Demo-Projekt für Teams, die im Rahmen der Digitalisierung der Stadt Zürich moderne, eventgetriebene Architekturen kennenlernen und adaptieren wollen.

---

## 🌍 Architekturübersicht

- **Frontend**: Angular 17+ Standalone App mit Formular für Anträge
- **Backend 1**: Spring Boot REST-API mit PostgreSQL (Antragsannahme)
- **Backend 2**: Kafka Consumer "Benachrichtigung" (loggt neue Anträge)
- **Backend 3**: Kafka Consumer "Archivierung" (speichert Anträge als Dummy-PDF-Datei)
- **Middleware**: Kafka + Kafka UI via Docker Compose
- **Persistenz**: PostgreSQL 15 via Docker

---

## 📊 Tagesplan: 5 Tage à 8h

### ✅ **Tag 1: Grundgerüst & Kafka**

- Projektstruktur: `Mini-VeVV-Demo` mit `docker/`, `antragsservice/`, `benachrichtigungsservice/`, `archivierungsservice/`, `frontend/`
- `docker-compose.yml` mit:
  - Kafka, Zookeeper
  - PostgreSQL
  - Kafka UI (port 8088)
- Kafka Hello World:
  - Spring Boot Producer schickt JSON-Message
  - Consumer loggt die Nachricht
- Angular CLI Setup mit `ng new frontend`
- Test: Angular -> Spring Boot -> Kafka -> Log

### ✅ **Tag 2: REST + Formular-Antrag**

- `POST /api/antrag` speichert Antrag in DB (PostgreSQL)
- Kafka Producer sendet JSON-Nachricht
- Angular-Formular bindet via `HttpClient.post()` an REST
- JSON Schema: `id`, `vorname`, `nachname`, `anliegen`
- Man kann lokal per Formular einen Antrag absenden

### ✅ **Tag 3: Kafka Consumer + Reaktion**

- Service 2: Kafka Consumer (benachrichtigungsservice) loggt Antrag
- Service 3: Kafka Consumer (archivierungsservice) schreibt Datei in `./archiv`
- Erstellung eines einfachen PDF-ähnlichen Textformats
- Konsumenten sind Teil eigener Consumer-Gruppen

### ✅ **Tag 4: Usability & Kafka UI**

- Angular zeigt nach POST eine Erfolgsmeldung
- `GET /api/antrag` gibt alle Anträge zurück
- Angular zeigt Liste aller Anträge unterhalb des Formulars
- Kafka UI: Live-Topic Überwachung unter [http://localhost:8088](http://localhost:8088)

### ✅ **Tag 5: Doku, Startskript, PDF, GitHub**

- `README.md` mit Setup- und Architekturbeschreibung
- `docker-compose up -d` startet Kafka + PostgreSQL + Kafka UI
- Spring Boot Services lokal starten (VS Code oder CLI)
- Angular per `ng serve` starten
- (Optional) Generierung einer echten PDF via Java Library

---

## 🚀 Schnellstart

### 1. Repository clonen
```bash
git clone https://github.com/helddigital/mini-vevv-demo.git
cd mini-vevv-demo
```

### 2. Infrastruktur starten (Kafka, PostgreSQL)
```bash
cd docker
docker-compose up -d
```

### 3. Spring Boot Backends starten
In separaten Terminals:
```bash
cd antragsservice && ./mvnw spring-boot:run
cd benachrichtigungsservice && ./mvnw spring-boot:run
cd archivierungsservice && ./mvnw spring-boot:run
```

### 4. Angular starten
```bash
cd frontend
ng serve
```

### 5. UI aufrufen
[http://localhost:4200](http://localhost:4200)  
Formular ausfüllen, abschicken, Kafka verfolgen

### 6. Kafka UI (Debug)
[http://localhost:8088](http://localhost:8088)  
Cluster: `local`  → Topic `antraege`

---

## 🧱 Warum das beeindruckt (Pitch für VeVV)

- ✨ **Ende-zu-Ende digitalisiert**: Vom Antrag bis zur Archivierung alles automatisiert
- ⚙️ **Technologietransfer-fähig**: Spring Boot, Kafka, Angular – 100% Open Source, sofort adaptierbar
- ⚡️ **Realistische Architektur**: Mikroservices, Events, Consumer-Gruppen, Scaling-ready
- 📜 **Dokumentierbar & auditierbar**: Jeder Antrag als JSON + Datei archiviert
- 🔎 **Transparenz für Teams**: Kafka UI + Logs + REST-Debugging
- 📲 **Frontend-Basis vorhanden**: Angular ready, erweiterbar für Benutzerkonto / Admin

---

## ✅ TODO / Erweiterungsideen
- Antragsstatus via Kafka aktualisieren (z.B. "in Bearbeitung")
- PDF-Ausgabe via iText / Apache PDFBox
- User-Login über „Mein Zürikonto“ simulieren
- Deployment auf Raspberry Pi (für lokale Demos ohne Cloud)
- CI/CD mit GitHub Actions (Build + Test + Lint + Docker)

---

## 🎩 Maintainer
**Christian Held**  
[helddigital.com](https://helddigital.com)  
Open Government Tech, AI & Open Source

