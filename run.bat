@echo off
setlocal enabledelayedexpansion
title TAK Limited - Tenant Management System
cd /d "%~dp0"

echo ===================================================
echo     TAK Limited - Tenant Management System
echo ===================================================
echo.

REM 1. Check if current Java works
java -version >nul 2>&1
if %ERRORLEVEL% EQU 0 goto JAVA_READY

REM 2. Check local portable .jdk folder
if exist "%~dp0.jdk" (
    for /d %%D in ("%~dp0.jdk\jdk*" "%~dp0.jdk") do (
        if exist "%%D\bin\java.exe" (
            set "JAVA_HOME=%%D"
            set "PATH=%%D\bin;!PATH!"
            goto JAVA_CHECK_AGAIN
        )
    )
)

REM 3. Check existing system installations
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" (
    set "PATH=%JAVA_HOME%\bin;!PATH!"
    goto JAVA_CHECK_AGAIN
)

if exist "D:\Java\jdk-25\bin\java.exe" (
    set "JAVA_HOME=D:\Java\jdk-25"
    set "PATH=D:\Java\jdk-25\bin;!PATH!"
    goto JAVA_CHECK_AGAIN
)

for /d %%D in ("C:\Program Files\Java\jdk*" "C:\Program Files\Eclipse Adoptium\jdk*" "C:\Program Files\Microsoft\jdk*" "C:\Program Files\Amazon Corretto\jdk*" "%USERPROFILE%\.jdks\*") do (
    if exist "%%D\bin\java.exe" (
        set "JAVA_HOME=%%D"
        set "PATH=%%D\bin;!PATH!"
        goto JAVA_CHECK_AGAIN
    )
)

:JAVA_CHECK_AGAIN
java -version >nul 2>&1
if %ERRORLEVEL% EQU 0 goto JAVA_READY

REM 4. If no working Java, automatically download portable OpenJDK 21
echo [INFO] Java is not detected on this computer.
echo [INFO] Automatically downloading portable OpenJDK 21 (Zero-Setup Mode)...
echo [INFO] Please wait a moment while it sets up (one-time download)...
echo.
powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference = 'Stop'; Write-Host 'Downloading OpenJDK 21...' -ForegroundColor Cyan; $url = 'https://api.adoptium.net/v3/binary/latest/21/ga/windows/x64/jdk/hotspot/normal/eclipse'; $zip = Join-Path '%~dp0' 'jdk.zip'; $dest = Join-Path '%~dp0' '.jdk'; (New-Object System.Net.WebClient).DownloadFile($url, $zip); Write-Host 'Extracting OpenJDK 21...' -ForegroundColor Cyan; Expand-Archive -Path $zip -DestinationPath $dest -Force; Remove-Item $zip -Force; Write-Host 'OpenJDK 21 setup completed!' -ForegroundColor Green;"

for /d %%D in ("%~dp0.jdk\jdk*" "%~dp0.jdk") do (
    if exist "%%D\bin\java.exe" (
        set "JAVA_HOME=%%D"
        set "PATH=%%D\bin;!PATH!"
        goto JAVA_READY
    )
)

echo [ERROR] Could not set up Java runtime automatically.
echo Please ensure your internet is connected or install JDK 21+.
pause
exit /b 1

:JAVA_READY
echo [OK] Java runtime detected:
java -version
echo.

powershell -NoProfile -Command "try { $c = New-Object Net.Sockets.TcpClient; $c.Connect('127.0.0.1', 3306); Write-Host '[OK] MySQL server detected on port 3306.' -ForegroundColor Green; $c.Close() } catch { Write-Host '[INFO] MySQL not detected. App will run in Zero-Setup mode using Embedded Database!' -ForegroundColor Cyan }"

echo.
echo Launching application via Maven Wrapper...
echo (All dependencies will be automatically verified and downloaded if missing)
echo.

call "%~dp0mvnw.cmd" javafx:run -f "%~dp0pom.xml"

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Application exited with code %ERRORLEVEL%.
    pause
)
