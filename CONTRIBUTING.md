# Contributing to RahimunishaMart

Thank you for contributing to the **RahimunishaMart** Capstone platform. Follow these step-by-step instructions to get a local development instance running from source.

---

## 1. Prerequisites
- **Java SE Development Kit (JDK)**: Version 17 or higher (`java -version`)
- **Apache Maven**: Version 3.8+ (`mvn -v`)
- **Git**: Version 2.20+ (`git --version`)
- **Web Browser**: Chrome, Firefox, Safari, or Edge

---

## 2. Quickstart: From Git Clone to Running Instance

### Step 1: Clone the Repository
```bash
git clone https://github.com/your-username/rahimunishamart.git
cd rahimunishamart
```

### Step 2: Environment Configuration
Copy the sample environment file to `.env` (or let the application use automatic zero-config defaults):
```bash
cp .env.example .env
```
> **Security Notice**: `.env` and `src/main/resources/config.properties` are listed in `.gitignore` and must never be committed to source control.

### Step 3: Build the Project and Run Tests
Verify that all unit tests, DAO tests with embedded H2, and code quality checks pass:
```bash
mvn clean verify
```

### Step 4: Run the Application Locally
You can run the application in two ways:

#### Option A: One-Click Standalone Embedded Runner (Fastest)
Compile and execute the embedded Tomcat server:
```bash
# On Windows
run.bat

# Or using Maven exec
mvn compile exec:java -Dexec.mainClass="com.rahimunisha.rahimunishamart.server.EmbeddedServer"
```

#### Option B: Deploy to Local Apache Tomcat 9
Copy the compiled WAR package from `target/` into your Apache Tomcat `webapps/` folder:
```bash
cp target/rahimunishamart.war /path/to/tomcat/webapps/ROOT.war
# Start Tomcat
/path/to/tomcat/bin/catalina.sh run
```

---

## 3. Verifying the Installation

Open your browser and navigate to:
- **Storefront**: [http://localhost:8080](http://localhost:8080)
- **Health Check**: [http://localhost:8080/api/v1/health](http://localhost:8080/api/v1/health) (Expected output: `{"status":"UP","db":"UP"}`)

### Pre-seeded Demo Credentials
| Role | Email | Password |
|---|---|---|
| **Administrator** | `admin@rahimunishamart.com` | `Password@123` |
| **Seller** | `techseller@rahimunishamart.com` | `Password@123` |
| **Buyer** | `nisha@rahimunishamart.com` | `Password@123` |

---

## 4. Git Workflow & Standing Rules
1. **Conventional Commit Prefixes**: Every commit must use conventional prefixes:
   - `feat:` for new capabilities
   - `fix:` for bug fixes
   - `test:` for test additions/updates
   - `docs:` for documentation updates
2. **Branching Model**: Keep `main` deployable at all times. Feature development occurs on `feature/<name>` branches and merges via pull request.
3. **Definition of Done**: Clean compilation, zero SQL concatenation (PreparedStatement only), passing tests, and updated documentation.
