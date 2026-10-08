@echo off
echo ===================================================================
echo     RAHIMUNISHA MART - ANNA UNIVERSITY CAPSTONE RUNNER
echo ===================================================================
echo.

where mvn >nul 2>nul
if %ERRORLEVEL% equ 0 (
    echo [INFO] Found Maven in PATH. Compiling and launching embedded Tomcat...
    mvn compile exec:java -Dexec.mainClass="com.rahimunisha.rahimunishamart.server.EmbeddedServer"
    goto end
)

where javac >nul 2>nul
if %ERRORLEVEL% equ 0 (
    echo [INFO] Maven not in PATH, but JDK detected.
    echo [INFO] Please install Maven or add Maven to PATH:
    echo        1. Download Apache Maven from https://maven.apache.org/download.cgi
    echo        2. Extract to C:\Program Files\apache-maven and add bin\ to PATH
    echo        3. Then run: mvn clean verify
    echo.
) else (
    echo [ERROR] JDK and Maven not detected. Please install JDK 17+ and Apache Maven.
)

pause
:end
