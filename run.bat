@echo off
setlocal enabledelayedexpansion
title TAK Limited - Tenant Management System

echo ===================================================
echo     TAK Limited - Tenant Management System
echo ===================================================
echo.

REM 1. Check for Java
where java >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    if defined JAVA_HOME (
        set "PATH=%JAVA_HOME%\bin;%PATH%"
    ) else (
        REM Check common Java locations
        if exist "D:\Java\jdk-25\bin\java.exe" (
            set "JAVA_HOME=D:\Java\jdk-25"
            set "PATH=D:\Java\jdk-25\bin;%PATH%"
        ) else if exist "C:\Program Files\Java" (
            for /d %%D in ("C:\Program Files\Java\jdk*") do (
                set "JAVA_HOME=%%D"
                set "PATH=%%D\bin;%PATH%"
            )
        )
    )
)

where java >nul 2>&1
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Java is not detected on your system.
    echo Please install JDK 21 or higher (e.g. from https://adoptium.net/ or via winget: winget install Oracle.JDK.25)
    echo.
    pause
    exit /b 1
)

echo [OK] Java detected:
java -version
echo.

REM 2. Check for MySQL Port 3306
powershell -Command "$t = New-Object Net.Sockets.TcpClient; try { $t.Connect('127.0.0.1', 3306); Write-Host '[OK] MySQL server is running on port 3306.'; $t.Close() } catch { Write-Host '[WARNING] MySQL is not responding on port 3306. Make sure MySQL service is running and tak_limited database is imported via schema.sql.' -ForegroundColor Yellow }"

echo.
echo Launching application via Maven Wrapper...
echo (All JavaFX and MySQL dependencies will be automatically verified and downloaded if missing)
echo.

call "%~dp0mvnw.cmd" javafx:run -f "%~dp0pom.xml"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [INFO] javafx:run exited with code %ERRORLEVEL%.
    pause
)
