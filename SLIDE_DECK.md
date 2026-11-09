# E-Commerce Shopping Mart — 9-Slide Presentation Deck
**Anna University R2025 Regulations — Final Project Defense & Viva**  
**Project Title**: E-Commerce Shopping Mart (Enterprise Multi-Vendor Platform)  
**Student Name**: Nisha / Rahimunisha  

---

## 📽️ Slide 1: Title Slide
### **Slide Title: E-Commerce Shopping Mart**
- **Subtitle**: Enterprise-Grade Multi-Vendor E-Commerce Platform with Pluggable AI Shopping Assistant
- **Curriculum Context**: Anna University R2025 Regulations — Semester 3 Capstone Defense
- **Candidate Name**: Nisha / Rahimunisha
- **Core Technology Stack**: Java 17 LTS | Java Servlets 4.0 | Apache Tomcat 9.0 | HikariCP | H2 Database | Google Gemini AI
- **Deployment**: Multi-Stage Docker Container on Render.com Cloud Platform

> **🗣️ Presenter Speaking Notes (Viva Talking Points):**
> *"Respected evaluators, good morning. Today I am presenting 'E-Commerce Shopping Mart', an enterprise-grade multi-vendor platform engineered strictly according to Anna University R2025 curriculum specifications using core Java Servlets 4.0, JDBC, and Apache Tomcat 9, augmented with an intelligent domain-scoped AI conversational assistant."*

---

## 📽️ Slide 2: Problem Statement & Project Objectives
### **Slide Title: Problem Statement & Objectives**
- **Challenges in Contemporary E-Commerce Platforms**:
  - **Data Inconsistency & Race Conditions**: Inadequate transaction isolation causes stock overselling during simultaneous checkouts.
  - **SQL Vulnerabilities**: Dynamic string concatenation in legacy JDBC exposing databases to SQL injection attacks.
  - **Resource Exhaustion**: Creating unpooled database connections causing high memory consumption and connection timeouts.
  - **Delayed Customer Support**: Static websites lack instant, automated resolution for shipping, return, and order tracking inquiries.
- **Key Project Objectives**:
  - Architect a resilient 3-tier enterprise structure strictly adhering to Java EE web standards.
  - Guarantee 100% ACID compliance and atomic stock deductions during order placement.
  - Implement zero-vulnerability data persistence using 100% parameter-bound `PreparedStatement` queries.
  - Maximize throughput with high-performance HikariCP connection pooling (< 10ms query latency).
  - Embed an intelligent, dual-mode conversational AI assistant (Google Gemini LLM + Offline FAQ fallback).

> **🗣️ Presenter Speaking Notes:**
> *"Existing academic and prototype applications often fail in concurrency and security. My primary objective was to deliver a production-grade system eliminating race conditions with atomic ACID transactions, safeguarding data with parameterized queries, and empowering buyers through integrated AI assistance."*

---

## 📽️ Slide 3: System Architecture & Design Patterns
### **Slide Title: Layered Architecture & Design Patterns**
- **Strict 3-Tier Layered Architecture**:
  1. **Presentation Layer**: Front Servlets (`HttpServlet`), Request Encoders, RBAC Filters (`AuthFilter`), and JSP Views with JSTL 1.2.
  2. **Business Logic Layer**: Domain Services (`CartService`, `OrderService`, `ProductService`, `UserService`, `ChatService`).
  3. **Data Access Layer**: DAO interfaces (`UserDAO`, `ProductDAO`, `OrderDAO`, `CartDAO`, `WishlistDAO`, `ReviewDAO`) and JDBC Implementations.
- **Enterprise Design Patterns Implemented**:
  - **Front Controller Pattern**: Standardized request routing and session validation across all endpoints.
  - **Data Access Object (DAO) Pattern**: Complete separation of relational queries from business operations.
  - **Singleton Pattern**: Managed lifecycle for connection pools (`DatabaseUtil`) and configuration loaders (`ConfigUtil`).
  - **Strategy Pattern**: Decoupled AI architecture switching dynamically between `GeminiChatProvider` and `MockChatProvider`.
  - **Factory Pattern**: `ChatProviderFactory` dynamically instantiating appropriate AI providers based on environment keys.

> **🗣️ Presenter Speaking Notes:**
> *"The project enforces clean architectural boundaries. No controller accesses the database directly; every user action flows through the Service layer, which validates business rules before invoking the DAO layer. We also adopted the Strategy Pattern for pluggable AI integration."*

---

## 📽️ Slide 4: Technology Stack & Specifications
### **Slide Title: Technology Stack & Technical Specifications**
- **Core Platform & Language**: Java SE 17 LTS (Modern Long-Term Support Java runtime).
- **Web & Servlet Container**: Apache Tomcat 9.0.86 (`javax.servlet` API 4.0.1 compliance).
- **Connection Pool**: HikariCP 5.1.0 (Zero-overhead JDBC connection management).
- **Database Engine**: H2 Relational Database 2.2.x (MVStore File-Persistence & In-Memory Unit Testing).
- **Password Cryptography**: jBCrypt 0.4 (Blowfish adaptive salt hashing with work factor 10).
- **JSON Serialization**: Google Gson 2.10.1 (Strict standard API envelopes: `{success, data, error}`).
- **View Templates**: JavaServer Pages (JSP 2.3) + JSTL 1.2 Core/Formatting Tags with XSS escaping.
- **DevOps & Cloud Runtime**: Multi-stage Docker container deployed on Render.com with dynamic port mapping.

> **🗣️ Presenter Speaking Notes:**
> *"Our tech stack avoids heavy proprietary frameworks like Spring, demonstrating mastery over foundational Java EE web standards. HikariCP ensures optimal connection pooling, while jBCrypt protects credentials using salted Blowfish hashing."*

---

## 📽️ Slide 5: Core Functional Capabilities (F1 – F8)
### **Slide Title: Core Functional Modules (F1 to F8)**
- **F1 (Authentication & RBAC)**: Role-based registration & authentication for Buyers, Sellers, and pre-seeded Admins with session rotation.
- **F2 (Seller Inventory Management)**: Complete product catalog management (Create, Read, Update, Delete) with image URLs and real-time inventory counts.
- **F3 (Product Catalog Discovery)**: Category filtering (Electronics, Fashion, Groceries, Home & Living), keyword search, and pagination.
- **F4 (Dynamic Cart Engine)**: Session-aware item aggregation, quantity update bounds checking, and instant running subtotal calculation.
- **F5 (Atomic ACID Checkout)**: Multi-step checkout verifying live stock levels, creating order items, and deducting inventory atomically in a single SQL transaction.
- **F6 (Order Visibility)**: Comprehensive chronological order history for buyers and fulfillment order queues for sellers.
- **F7 (Admin Governance)**: Administrative oversight panel for catalog auditing, platform metrics, and user management.
- **F8 (Verified Customer Reviews)**: 5-star customer rating system restricted strictly to buyers who have completed orders for that item.

> **🗣️ Presenter Speaking Notes:**
> *"All eight fundamental functional specifications outlined in the capstone curriculum are fully implemented. Crucially, in Module F5, checkouts execute within an explicit database transaction rollback block, guaranteeing that inventory cannot be oversold even under high concurrency."*

---

## 📽️ Slide 6: Advanced Extensions & AI Chatbot Subsystem (O1 – O4)
### **Slide Title: Advanced Features & AI Subsystem (O1 to O4)**
- **O1 (Saved Wishlist Module)**: Persistent multi-device product bookmarking with 1-click "Move to Cart" and instant removal.
- **O2 (Order Lifecycle State Machine)**: Deterministic progression through explicit order states:
  $$\text{PENDING} \longrightarrow \text{CONFIRMED} \longrightarrow \text{SHIPPED} \longrightarrow \text{DELIVERED}$$
  Includes customer cancellation guardrails prior to shipping.
- **O3 (Seller Revenue & Sales Dashboard)**: Merchant analytics displaying cumulative sales volume, revenue metrics, and inventory restock alerts.
- **O4 (AI Shopping Assistant Subsystem)**:
  - **Cloud LLM Mode**: Google Gemini 1.5 Flash integration with domain-constrained system prompts.
  - **Offline FAQ Mode**: Zero-latency rule-based matching covering 11+ e-commerce policies (Shipping, Refunds, Warranties, COD).
  - **Security & Caching**: 500-character input caps, HTML escaping, and session-level query caching for sub-millisecond repeated queries.

> **🗣️ Presenter Speaking Notes:**
> *"Beyond the baseline deliverables, we engineered all four optional extensions. The standout is O4: an AI Shopping Assistant that handles shopper inquiries using Google Gemini LLM when online, and smoothly falls back to an offline FAQ rule engine if no internet connectivity is available."*

---

## 📽️ Slide 7: Database Design & Security Compliance
### **Slide Title: Relational Schema & Security Architecture**
- **Relational Schema (7 Normalized Entities)**:
  - `users` (id, email, password_hash, full_name, role, phone, address, created_at)
  - `products` (id, seller_id, name, description, category, price, stock_qty, image_url, status)
  - `orders` (id, buyer_id, total_amount, status, shipping_address, payment_method, payment_status)
  - `order_items` (id, order_id, product_id, quantity, unit_price)
  - `cart_items` (id, user_id, product_id, quantity, created_at)
  - `wishlist_items` (id, user_id, product_id, created_at)
  - `reviews` (id, product_id, user_id, rating, comment, created_at)
- **Rigorous Security Checklist Compliance**:
  - **SQL Injection Defense**: 100% parameter-bound `PreparedStatement` queries. Zero string concatenation.
  - **Credential Security**: Passwords salted and hashed with BCrypt. Plaintext passwords never stored or logged.
  - **Session Fixation Prevention**: `req.changeSessionId()` executed upon successful authentication.
  - **Access Control (`AuthFilter`)**: Enforces URL path protections and role verification (Buyer / Seller / Admin).
  - **Information Disclosure Mitigation**: Custom HTTP 403, 404, and 500 error pages suppressing server stack traces.

> **🗣️ Presenter Speaking Notes:**
> *"Our database is fully normalized with foreign key relationships and index optimizations. From a security standpoint, we strictly comply with OWASP guidelines: passwords are never stored in plaintext, session IDs rotate on login to prevent session fixation, and all database queries utilize PreparedStatements."*

---

## 📽️ Slide 8: Testing, Verification & Cloud Deployment
### **Slide Title: Verification, Quality Assurance & Cloud CI/CD**
- **Comprehensive Automated Testing**:
  - **Test Suite**: Built with JUnit 5 Jupiter and Mockito 5.
  - **Coverage**: 16/16 automated test cases passing with 100% success rate across DAO layers, UserService, ProductService, and OrderService.
  - **Persistence Testing**: In-memory isolated H2 testing verifying transaction rollbacks and constraint enforcements.
- **Code Quality & Static Analysis**:
  - Integration with Maven Checkstyle and SpotBugs for zero defect tolerance.
- **Cloud Deployment Pipeline**:
  - **Containerization**: Multi-stage `Dockerfile` (Build in Maven Temurin-17, execute in stripped Tomcat 9).
  - **Platform**: Hosted live on Render.com cloud infrastructure.
  - **Live URL**: Connected via automated GitHub CI/CD webhooks triggered on `main` branch push.
  - **Health Monitoring**: Dedicated `/api/v1/health` endpoint returning real-time DB and Servlet status.

> **🗣️ Presenter Speaking Notes:**
> *"Software engineering quality is verified through our automated test suite comprising 16 unit and integration test cases covering services and persistence. The entire application is packaged via a multi-stage Docker container and deployed live on Render cloud with automated GitHub CI/CD integration."*

---

## 📽️ Slide 9: Demonstration Walkthrough, Conclusion & Future Scope
### **Slide Title: Live Demonstration, Future Scope & Conclusion**
- **Demonstration Flow**:
  1. **Buyer Flow**: Register/Login ➔ Explore catalog with category filtering ➔ Save to Wishlist ➔ Add to Cart ➔ Atomic Checkout ➔ View Order Status.
  2. **AI Assistant Demo**: Query shipping timescales, return conditions, and payment options with instant responses.
  3. **Seller Flow**: Access merchant dashboard ➔ Inspect incoming orders ➔ Advance status to `SHIPPED`.
  4. **Admin Flow**: View platform-wide operational metrics and manage product listings.
- **Future Enhancements**:
  - Integration of live production payment gateways (Razorpay / Stripe webhook processing).
  - Distributed caching and session clustering using Redis.
  - Elasticsearch integration for typo-tolerant fuzzy product search.
- **Conclusion**:
  - Fully realized, robust Java EE Capstone compliant with Anna University R2025 guidelines.
  - Demonstrates strong architectural principles, database transaction integrity, and modern cloud deployment.

> **🗣️ Presenter Speaking Notes:**
> *"In summary, E-Commerce Shopping Mart proves that core Java enterprise technologies, when architected properly with clean separation of concerns and robust security practices, provide an exceptionally fast, reliable, and scalable foundation. Thank you, and I am now open to questions from the evaluation committee."*
