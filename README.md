# RahimunishaMart — Enterprise E-Commerce Platform
[![Java 17](https://img.shields.io/badge/Java-17%20LTS-orange.svg)](https://www.oracle.com/java/)
[![Tomcat 9](https://img.shields.io/badge/Apache%20Tomcat-9.0.x-blue.svg)](https://tomcat.apache.org/)
[![HikariCP](https://img.shields.io/badge/HikariCP-5.1.0-brightgreen.svg)](https://github.com/brettwooldridge/HikariCP)
[![License](https://img.shields.io/badge/License-Academic-lightgrey.svg)]()

> **Anna University R2025, Semester 3 Capstone Project**  
> **Student Builder**: Rahimunisha | **Package**: `com.rahimunisha.rahimunishamart`

---

## 📋 Table of Contents
1. [Project Overview](#project-overview)
2. [Tech Stack Summary](#tech-stack-summary)
3. [Architecture & Design Patterns](#architecture--design-patterns)
4. [Architectural Diagrams (D1, D2, D3)](#architectural-diagrams)
5. [Feature Matrix (F1–F8 & O1–O4)](#feature-matrix)
6. [Security Checklist Compliance](#security-checklist-compliance)
7. [Getting Started & Local Setup](#getting-started--local-setup)
8. [Test Accounts & Demo Walkthrough](#test-accounts--demo-walkthrough)
9. [Deployment Guide](#deployment-guide)

---

## 🌟 Project Overview
**RahimunishaMart** is an enterprise-grade multi-vendor e-commerce platform built strictly with **Java Servlets 4.0 (`javax.servlet.*`)**, **JDBC**, and **Apache Tomcat 9.0.x**, complying fully with Anna University R2025 semester capstone standards.

The application delivers end-to-end shopping journeys for Buyers, dedicated inventory and fulfillment tools for Sellers, administrative moderation dashboards, and a pluggable AI shopping assistant powered by Google Gemini and offline FAQ rules.

---

## 🛠️ Tech Stack Summary

| Layer | Technologies & Libraries | Purpose |
|---|---|---|
| **Runtime & Language** | Java SE 17 LTS | Core object-oriented server platform |
| **Servlet Container** | Apache Tomcat 9.0.x (`javax.servlet 4.0.1`) | Web request handling & container lifecycle |
| **Connection Pooling** | HikariCP 5.1.0 | High-performance JDBC connection pool |
| **Relational Database** | H2 Database Engine 2.2.x | File-persistent and in-memory transactional database |
| **Password Security** | jBCrypt 0.4 | Adaptive salt password hashing |
| **JSON Serialization** | Google Gson 2.10.1 | Envelope formatting `{success, data, error}` |
| **View Rendering** | JSP 2.3 + JSTL 1.2 | Server-side template rendering with XSS escaping |
| **AI Subsystem** | Strategy Pattern (Gemini 1.5 Flash + Mock FAQ) | Domain-scoped conversational assistant |
| **Unit & Integration Testing** | JUnit 5 Jupiter + Mockito 5.11 | DAO & Service layer test verification |
| **Static Code Analysis** | Checkstyle 3.3.1 + SpotBugs 4.8.3 | Code style and defect detection |

---

## 🏛️ Architecture & Design Patterns

The project enforces strict physical separation of concerns:
- **Presentation Tier** (`com.rahimunisha.rahimunishamart.controller`, `filter`, `listener`)
- **Business Logic Tier** (`com.rahimunisha.rahimunishamart.service`)
- **Data Access Tier** (`com.rahimunisha.rahimunishamart.dao`, `dao.impl`)
- **Data Transfer Objects** (`com.rahimunisha.rahimunishamart.dto`)

### Design Patterns Utilized
- **DAO Pattern**: Encapsulates persistence logic away from business services.
- **Front Controller Pattern**: Standardized HTTP request routing and parameter dispatching.
- **Singleton Pattern**: Centralized `DatabaseUtil` and `ConfigUtil` instances.
- **Factory Pattern**: `ChatProviderFactory` creates the configured AI engine.
- **Strategy Pattern**: Interchangeable `ChatProvider` implementations (`MockChatProvider` vs `GeminiChatProvider`).
- **Builder / Envelope Pattern**: Standardized `ApiResponse<T>` wrappers for JSON endpoints.

---

## 📊 Architectural Diagrams

- 📐 **[D1 Entity-Relationship Diagram](D1_ER_DIAGRAM.md)**: Full relational schema detailing tables, foreign key indexes, and DECIMAL(10,2) attributes.
- 📐 **[D2 Use Case Diagram](D2_USE_CASE_DIAGRAM.md)**: Actor interactions for Buyers, Sellers, Admins, and Guests spanning F1–F8 and O1–O4.
- 📐 **[D3 Sequence Diagram](D3_SEQUENCE_DIAGRAM.md)**: Place-order checkout flow depicting atomic ACID transactions and rollback handling.

---

## 🎯 Feature Matrix

| Feature ID | Description | Status |
|---|---|---|
| **F1** | Authentication & RBAC (Buyer & Seller signup, Seed Admin, jBCrypt, Session Regeneration) | ✅ Completed |
| **F2** | Seller Listing Management (Create, edit, delete listings, image URLs, stock tracking) | ✅ Completed |
| **F3** | Buyer Catalog Discovery (Keyword search, category filtering, pagination) | ✅ Completed |
| **F4** | Cart Engine (Add, update quantity, remove, running total calculation) | ✅ Completed |
| **F5** | Atomic Checkout (Mock payment simulation, transactional inventory decrement, cart clear) | ✅ Completed |
| **F6** | Order Management (Buyer order history and seller fulfillment dashboard) | ✅ Completed |
| **F7** | Admin Panel (User directory, orders audit, product listing moderation) | ✅ Completed |
| **F8** | Verified Customer Reviews (5-star ratings restricted strictly to verified purchasers) | ✅ Completed |
| **O1** | Wishlist / Save-for-Later (Save items for future checkout) | ✅ Completed |
| **O2** | Order Status Lifecycle (`Pending ➔ Confirmed ➔ Shipped ➔ Delivered`) | ✅ Completed |
| **O3** | Seller Sales Analytics (Total revenue, units sold, low-stock alerts) | ✅ Completed |
| **O4** | AI Conversational Assistant (Floating widget, Gemini LLM + Mock FAQ, rate-limiting) | ✅ Completed |

---

## 🔒 Security Checklist Compliance (Section 9)

- ✅ **SQL Injection Defense**: 100% of queries use `PreparedStatement`. Zero SQL string concatenation.
- ✅ **Password Security**: Passwords hashed with `jBCrypt` (workload 10); passwords never logged.
- ✅ **Session Fixation Defense**: `request.changeSessionId()` called on login; 30-min timeout in `web.xml`.
- ✅ **Role-Based Authorization**: `AuthFilter` protects all sensitive routes (`/seller/*`, `/admin/*`, `/cart/*`, `/checkout`).
- ✅ **XSS Defense**: All user-controlled variables escaped via `<c:out value="..." />`.
- ✅ **Stack Trace Suppression**: Custom error pages (`403.jsp`, `404.jsp`, `500.jsp`) prevent leakage of internal stack traces.
- ✅ **Credential Hygiene**: Sensitive credentials stored in `.env` / environment variables, ignored by `.gitignore`.

---

## 🚀 Getting Started & Local Setup

### Prerequisites
- JDK 17+
- Apache Maven 3.8+

### Quick Start
```bash
# 1. Clone repository
git clone https://github.com/your-username/rahimunishamart.git
cd rahimunishamart

# 2. Setup environment variables (optional, zero-config defaults are included)
cp .env.example .env

# 3. Build & verify tests
mvn clean verify

# 4. Run application (Embedded Tomcat Runner)
run.bat
# Or on Unix:
mvn compile exec:java -Dexec.mainClass="com.rahimunisha.rahimunishamart.server.EmbeddedServer"
```

Open your browser to:
- **Application**: [http://localhost:8080](http://localhost:8080)
- **Health Check**: [http://localhost:8080/api/v1/health](http://localhost:8080/api/v1/health) (Returns `{"status":"UP","db":"UP"}`)

---

## 👥 Test Accounts & Demo Credentials

| Role | Email | Password |
|---|---|---|
| **Administrator** | `admin@rahimunishamart.com` | `Password@123` |
| **Seller** | `techseller@rahimunishamart.com` | `Password@123` |
| **Buyer** | `nisha@rahimunishamart.com` | `Password@123` |

*(Quick-fill buttons are conveniently available on the Sign In page for seamless evaluator review).*

---

## 🚢 Deployment Guide

### Docker Deployment
```bash
docker build -t rahimunishamart:v1.1.0 .
docker run -d -p 8080:8080 -v $(pwd)/data:/usr/local/tomcat/data rahimunishamart:v1.1.0
```

### Standalone Tomcat 9 Deployment
1. Run `mvn clean package -DskipTests` to produce `target/rahimunishamart.war`.
2. Copy `target/rahimunishamart.war` into your Tomcat `webapps/ROOT.war` directory.
3. Start Tomcat using `./bin/catalina.sh run`.

---

## 📄 Additional Project Documentation
- 📖 **[Final Engineering Report](FINAL_REPORT.md)**
- 📖 **[Rehearsed Demo Script](DEMO_SCRIPT.md)**
- 📖 **[Presentation Slide Deck](SLIDE_DECK.md)**
- 📖 **[Sprint Retrospectives](RETRO.md)**
- 📖 **[Contributing Guide](CONTRIBUTING.md)**
