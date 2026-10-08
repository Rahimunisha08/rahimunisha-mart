# RahimunishaMart — Capstone Final Engineering Report
**Anna University R2025, Semester 3 — Capstone Project**  
**Author / Solo Builder**: Rahimunisha  
**Technologies**: Java Servlets 4.0 · JDBC · Apache Tomcat 9.0 · HikariCP · H2 Database · AI Chatbot Proxy  
**Evaluation Window**: Jul 27 – Oct 10, 2026  

---

## 1. Executive Summary & Problem Statement
E-commerce ecosystems require high reliability, secure transactional boundaries, strict access controls, and real-time customer assistance. **RahimunishaMart** is an enterprise-grade multi-vendor e-commerce platform designed and developed to fulfill the rigorous Anna University R2025 engineering criteria.

The application allows buyers to seamlessly discover products, filter by categories, manage cart contents with running subtotals, execute mock payments, track orders through a standardized fulfillment lifecycle, and submit verified reviews. Concurrently, merchants gain a dedicated Seller Command Center to manage listings and fulfill orders, while administrators possess moderation capabilities across users and products. An AI-powered conversational assistant (O4) delivers instant, domain-restricted shopping guidance.

---

## 2. System Architecture & Tiered Layering

RahimunishaMart adheres to a strict multi-tier software architecture:

```
[ Web Browser Client ]
        │
   HTTP / HTTPS
        │
┌───────▼────────────────────────────────────────────────────────┐
│  Presentation Tier (Servlets, JSTL 1.2 Views, JS / CSS)        │
│  - AuthFilter (RBAC & Session Fixation Protection)             │
│  - EncodingFilter (UTF-8 & Security Headers)                   │
│  - Controllers: Auth, Product, Cart, Checkout, Order, Admin,   │
│                 SellerDashboard, Review, Chat                  │
└───────┬────────────────────────────────────────────────────────┘
        │ DTOs (Data Transfer Objects: Envelope {success, data})
┌───────▼────────────────────────────────────────────────────────┐
│  Business Logic Tier (Services)                                │
│  - UserService, ProductService, CartService, OrderService,     │
│    ReviewService, ChatService                                  │
│  - Input validation prior to database access                   │
└───────┬────────────────────────────────────────────────────────┘
        │ Domain Entities (User, Product, Order, Review, etc.)
┌───────▼────────────────────────────────────────────────────────┐
│  Data Access Tier (DAO Interfaces & JDBC Implementations)      │
│  - UserDAO, ProductDAO, CartDAO, OrderDAO, ReviewDAO           │
│  - PreparedStatement Only (No SQL Injection)                   │
│  - ACID Transactions (conn.setAutoCommit(false))               │
└───────┬────────────────────────────────────────────────────────┘
        │
┌───────▼────────────────────────────────────────────────────────┐
│  Persistence & Infrastructure Tier                             │
│  - HikariCP High-Performance Connection Pool                   │
│  - H2 Relational Database Engine (Server/File & In-Memory Mode)│
│  - External AI Gateway (Google Gemini 1.5 Flash / Mock FAQ)    │
└────────────────────────────────────────────────────────────────┘
```

---

## 3. Design Patterns Applied

| Design Pattern | Purpose & Implementation in RahimunishaMart |
|---|---|
| **Data Access Object (DAO)** | Decouples business logic from persistence operations (`UserDAO`, `ProductDAO`, `OrderDAO`, `CartDAO`, `ReviewDAO`, `WishlistDAO`). |
| **Front Controller / Servlet Dispatcher** | Centralizes HTTP request routing, input extraction, and view forwarding across standardized endpoints (`ProductServlet`, `CartServlet`, `SellerDashboardServlet`). |
| **Singleton Pattern** | Manages the global database connection pool (`DatabaseUtil`) and configuration reader (`ConfigUtil`), ensuring a single HikariDataSource instance. |
| **Factory Pattern** | Instantiates the appropriate `ChatProvider` dynamically (`ChatProviderFactory`) based on environment configuration (`mock` vs `gemini`). |
| **Strategy Pattern** | Pluggable AI engine implementation (`ChatProvider` interface with `MockChatProvider` and `GeminiChatProvider`), avoiding tight coupling to a single vendor. |
| **Builder / Envelope Pattern** | Encapsulates consistent API output structures (`ApiResponse<T>` with `{success, data, error}`). |

---

## 4. Key Architectural Diagrams

Detailed architectural diagrams have been produced in compliance with Anna University deliverables:
- **D1 — Relational Entity-Relationship Diagram**: Documented in `D1_ER_DIAGRAM.md`.
- **D2 — Functional Use Case Diagram**: Documented in `D2_USE_CASE_DIAGRAM.md`.
- **D3 — Checkout Sequence Diagram**: Documented in `D3_SEQUENCE_DIAGRAM.md`.

---

## 5. Security Checklist Compliance (Section 9)

1. **Parameterization**: Every SQL interaction is executed strictly via `PreparedStatement`. String concatenation in queries is prohibited.
2. **Password Security**: Passwords are encrypted using `jBCrypt` (workload factor 10). Plaintext passwords and hashes are never logged.
3. **Session Fixation Defense**: `request.changeSessionId()` is invoked upon successful authentication. Session timeouts are explicitly configured to 30 minutes in `web.xml`.
4. **Role-Based Access Control (RBAC)**: `AuthFilter` intercepts protected paths (`/seller/*`, `/admin/*`, `/cart/*`, `/checkout`) and denies unauthorized access with HTTP 401/403.
5. **Output Escaping**: User-controlled strings in JSP views are escaped via `<c:out value="..." />` to prevent cross-site scripting (XSS).
6. **Custom Error Handling**: Stack traces are masked via custom error pages (`403.jsp`, `404.jsp`, `500.jsp`).
7. **Secret Management**: Sensitive tokens and database passwords reside in `.env` / system environment variables, excluded from git via `.gitignore`.

---

## 6. AI Conversational Assistant (O4 & Section 11)

The integrated AI assistant satisfies all requirements:
- **Interface Decoupling**: Defined via `ChatProvider`.
- **Mock Provider**: Canned domain-specific answers covering 10+ e-commerce topics (returns, shipping, tracking, payments, seller onboarding).
- **Gemini Provider**: Outbound HTTP requests to Google Gemini 1.5 Flash API with server-side keys.
- **Resilience**: Outbound calls have a strict 6-second timeout; caught exceptions degrade gracefully into helpful canned responses.
- **Session Cache**: Identical queries per session are served instantly from memory (`ChatService`).
- **Rate Limiting**: Throttles queries to 10 requests per minute per session.
- **UI Integration**: Floating responsive widget at the lower-right corner of every page.

---

## 7. Known Limitations & Future Enhancements

1. **Real Payment Gateway**: Payments currently utilize a simulation step with instant approval; future versions will integrate Razorpay / Stripe webhooks.
2. **Distributed Session Storage**: Current session management relies on in-memory Tomcat sessions; future cloud deployments can transition to Redis-backed session clustering.
3. **Full-Text Search Engine**: Keyword searching uses SQL pattern matching; future versions could incorporate Apache Lucene or Elasticsearch.
