@echo off
cd /d "%~dp0"
if exist out rmdir /s /q out
mkdir out
javac -encoding UTF-8 -d out src\ec\edu\puce\modelo\*.java
if errorlevel 1 (
  echo.
  echo Error al compilar. Verifica que Java JDK este instalado.
  pause
  exit /b 1
)
java -cp out ec.edu.puce.modelo.ServidorWeb
pause
