# RahimunishaMart Capstone — Slide Deck
**Anna University R2025 Semester 3 — Final Project Defense**  
**Student Builder**: Rahimunisha | **Title**: RahimunishaMart Multi-Vendor Platform

---

## Slide 1: Title & Overview
- **Project**: RahimunishaMart E-Commerce Platform
- **Domain**: Enterprise Java Servlets, JDBC, Apache Tomcat 9.0
- **Scope**: Multi-vendor marketplace with Buyers, Sellers, Admins, and AI Chatbot
- **Builder**: Solo Builder (Rahimunisha)

---

## Slide 2: Problem Statement & Motivation
- **Context**: Fragmented online shopping platforms often suffer from poor transactional integrity, data inconsistency under concurrent checkouts, and unresponsive customer support.
- **Goal**: Architect a resilient, strictly layered multi-vendor platform adhering to Anna University R2025 specifications:
  - Zero SQL string concatenation (100% PreparedStatements)
  - Explicit connection pool lifecycle via HikariCP
  - Atomic ACID checkouts preventing out-of-stock race conditions
  - AI-assisted real-time customer query resolution

---

## Slide 3: System Architecture & Tech Stack
- **Architecture**: Strict 3-Tier Layered Architecture (Web / Service / DAO)
- **Tech Stack**:
  - **Runtime**: Java 17 LTS / Tomcat 9.0.x
  - **Data Layer**: HikariCP Connection Pooling + H2 Persistent Relational DB
  - **Security**: jBCrypt Password Hashing + Session ID Regeneration + RBAC Filter
  - **Frontend**: Responsive CSS3, JSTL 1.2 JSP Views, Vanilla JS Fetch API
  - **AI Subsystem**: Strategy Pattern with Gemini 1.5 Flash & Mock FAQ Fallback

---

## Slide 4: Core Features (F1 – F8)
- **F1 (Auth & RBAC)**: Buyer/Seller registration, BCrypt hashing, Seed Admin account.
- **F2 (Seller Listings)**: Full CRUD on product inventory with low-stock indicators.
- **F3 (Buyer Discovery)**: Category filtering, keyword search, and pagination.
- **F4 (Cart Engine)**: Real-time running total calculation and stock boundary enforcement.
- **F5 (Checkout Flow)**: Mock payment simulation with transactional atomic deductions.
- **F6 (Order Visibility)**: Buyer order history and merchant incoming fulfillment orders.
- **F7 (Governance)**: Admin moderation of flagged listings and user audits.
- **F8 (Verified Reviews)**: 5-star ratings restricted strictly to verified buyers.

---

## Slide 5: Optional Extensions & AI Assistant (O1 – O4)
- **O1 (Wishlist)**: Bookmarking products for later checkout.
- **O2 (Order Workflow)**: `Pending ➔ Confirmed ➔ Shipped ➔ Delivered` transitions.
- **O3 (Seller Sales Dashboard)**: Revenue analytics, units sold, and low-stock alerts.
- **O4 (AI Assistant)**: Pluggable AI engine answering 10+ e-commerce domain FAQs with session rate limiting, 500-char input cap, and in-memory caching.

---

## Slide 6: Engineering Rigor & Quality Metrics
- **Tests**: JUnit 5 + Mockito Service tests + Embedded H2 DAO tests.
- **Static Analysis**: Checkstyle & SpotBugs integrated into GitHub Actions CI pipeline.
- **Performance**: Sub-10ms response times for database queries via HikariCP.
- **Security Audit**: Custom error pages masking stack traces, XSS escaping, CSRF/Clickjacking headers.

---

## Slide 7: Live Demonstration Journey
1. **Buyer Journey**: Register ➔ Browse Catalog ➔ Add to Cart ➔ Checkout with Mock Payment ➔ Track Order.
2. **AI Support**: Click floating chat widget ➔ Query order tracking & returns.
3. **Seller Journey**: Login ➔ View Sales Dashboard ➔ Advance Order to "Shipped".
4. **Admin Journey**: Review global audit log ➔ Moderate product listing.

---

## Slide 8: Conclusion & Future Roadmap
- **Takeaways**: Demonstrated mastery of core Java web fundamentals, relational ACID transactions, and extensible software patterns.
- **Roadmap**: Live payment gateway integration (Razorpay), Redis session clustering, and Elasticsearch search refinement.
- **Q&A**: Open for faculty committee evaluation.
