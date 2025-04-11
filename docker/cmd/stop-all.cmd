@echo off
echo [🛑] Beende Spring Boot Microservices...

taskkill /FI "WINDOWTITLE eq Antragsservice" /T /F >nul 2>&1
taskkill /FI "WINDOWTITLE eq Benachrichtigung" /T /F >nul 2>&1
taskkill /FI "WINDOWTITLE eq Archivierung" /T /F >nul 2>&1

echo [🐳] Stoppe Docker-Container...
cd docker
docker-compose down -v

echo [✅] Alle Prozesse wurden beendet.
pause
