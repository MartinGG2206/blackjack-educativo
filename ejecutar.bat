@echo off
setlocal
cd /d "%~dp0"

where java >nul 2>nul
if errorlevel 1 (
  echo Java no esta disponible en PATH.
  echo Instala Java 17 o agrega java al PATH del sistema.
  pause
  exit /b 1
)

call "%~dp0mvnw.cmd" -q package
if errorlevel 1 (
  echo.
  echo No se pudo compilar o empaquetar la aplicacion.
  pause
  exit /b 1
)

start "" cmd /c java -jar "%~dp0target\blackjack-educativo-1.0.0.jar"
timeout /t 4 >nul
start "" http://localhost:8080
exit /b 0
