@echo off
setlocal
echo ===================================================================
echo     RAHIMUNISHA MART - ANNA UNIVERSITY CAPSTONE RUNNER
echo ===================================================================
echo.

set "MVN_CMD="

where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    set "MVN_CMD=mvn"
) else if exist "%~dp0apache-maven-3.9.9\bin\mvn.cmd" (
    set "MVN_CMD=%~dp0apache-maven-3.9.9\bin\mvn.cmd"
)

if defined MVN_CMD (
    echo [INFO] Using Maven: %MVN_CMD%
    echo [INFO] Starting RahimunishaMart Server on port 8080...
    call "%MVN_CMD%" exec:java
    goto end
)

echo [ERROR] Maven not found. Please ensure Java JDK 17+ and Maven are installed.
pause
:end
endlocal
