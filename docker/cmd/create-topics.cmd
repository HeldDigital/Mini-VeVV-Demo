@echo off
call wait-for-kafka.cmd

set KAFKA_CONTAINER=docker-kafka-1

echo [INFO] Erzeuge Topic antraege-validiert
docker exec %KAFKA_CONTAINER% kafka-topics --bootstrap-server localhost:29092 --create --if-not-exists --topic antraege-validiert --partitions 3 --replication-factor 1

echo [INFO] Erzeuge Topic antraege-abgelehnt
docker exec %KAFKA_CONTAINER% kafka-topics --bootstrap-server localhost:29092 --create --if-not-exists --topic antraege-abgelehnt --partitions 1 --replication-factor 1

echo [INFO] Erzeuge Topic archiv-fehler
docker exec %KAFKA_CONTAINER% kafka-topics --bootstrap-server localhost:29092 --create --if-not-exists --topic archiv-fehler --partitions 1 --replication-factor 1

echo [INFO] Erzeuge Topic dead-letter
docker exec %KAFKA_CONTAINER% kafka-topics --bootstrap-server localhost:29092 --create --if-not-exists --topic dead-letter --partitions 1 --replication-factor 1

echo [OK] Alle Topics wurden erstellt.
