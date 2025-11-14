@echo off
chcp 65001 >nul
echo ========================================
echo  PATRON FLYWEIGHT - COMPILACION
echo ========================================
echo.

if not exist bin mkdir bin

echo Compilando archivos Java...
javac -encoding UTF-8 -d bin -sourcepath src src/implementacion/*.java src/flyweight/*.java

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo  COMPILACION EXITOSA
    echo ========================================
    echo.
    echo Ejecutando programa...
    echo.
    java -cp bin flyweight.FlyweightMain
) else (
    echo.
    echo ========================================
    echo  ERROR EN LA COMPILACION
    echo ========================================
    pause
)

echo.
echo.
pause