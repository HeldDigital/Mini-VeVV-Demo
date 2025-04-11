@echo off
echo [INFO] Warte auf Kafka-Start (max. 60 Sekunden)...
setlocal
set count=0

:loop
docker exec docker-kafka-1 kafka-topics --bootstrap-server localhost:29092 --list >nul 2>&1
IF %ERRORLEVEL% EQU 0 (
    echo [OK] Kafka ist bereit.
    endlocal
    exit /b 0
)

set /a count+=1
if %count% GEQ 30 (
    echo [❌] Kafka-Startup hat zu lange gedauert. Abbruch nach 60 Sekunden.
    endlocal
    exit /b 1
)

timeout /t 2 >nul
goto loop
