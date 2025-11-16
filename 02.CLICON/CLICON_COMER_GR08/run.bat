@echo off
chcp 65001 >nul
cls
echo.
echo ════════════════════════════════════════════════════════════════
echo          CLIENTE CONSOLA - COMERCIALIZADORA MONSTER          
echo ════════════════════════════════════════════════════════════════
echo.
echo [1/2] Compilando proyecto...
call mvn clean compile -q
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Fallo en la compilacion
    pause
    exit /b 1
)
echo [OK] Compilacion exitosa
echo.
echo [2/2] Ejecutando aplicacion...
echo.
call mvn exec:java -q
echo.
echo.
echo Aplicacion finalizada.
pause
