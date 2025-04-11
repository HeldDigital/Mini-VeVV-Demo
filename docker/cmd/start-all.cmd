@echo off
setlocal enabledelayedexpansion

echo [🚀] Starte Docker Compose...
cd ..
docker-compose up -d

echo [🕒] Warte auf Kafka Startup...
timeout /t 10 >nul

echo [🧠] Initialisiere Kafka Topics...

set KAFKA_CONTAINER=docker-kafka-1
set TOPICS=antraege-validiert antraege-abgelehnt archiv-fehler dead-letter

FOR %%T IN (%TOPICS%) DO (
    echo [INFO] Erzeuge Topic %%T
    docker exec -i %KAFKA_CONTAINER% kafka-topics --create --if-not-exists --topic %%T --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
)

echo [✅] Kafka Topics wurden initialisiert.

echo [⚙️] Starte Spring Boot Microservices...

start "Antragsservice" cmd /k "cd ..\..\antragsservice && mvnw spring-boot:run"
start "Benachrichtigung" cmd /k "cd ..\..\benachrichtigungsservice && mvnw spring-boot:run"
start "Archivierung" cmd /k "cd ..\..\archivierungsservice && mvnw spring-boot:run"

echo [🌐] Kafka UI läuft unter: http://localhost:8088
endlocal
