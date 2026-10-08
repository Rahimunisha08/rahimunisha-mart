# Sprint Retrospective Log (RETRO.md)
## RahimunishaMart Capstone — Anna University R2025

Format: *What worked | What didn't | One change for next sprint*

* **Kickoff (Jul 24 – Jul 27)**: Maven and package scaffolding completed smoothly | Initial Tomcat deployment had classpath mismatch with servlet API | Lock down javax.servlet dependency version strictly in `pom.xml`.
* **Week 1 (Jul 27 – Aug 2)**: HikariCP connection pool setup and jBCrypt password hashing established | H2 in-memory test database required table creation before tests executed | Introduced automated schema execution script inside `AppContextListener`.
* **Week 2 (Aug 3 – Aug 9)**: End-to-end shopping journey (browse ➔ cart ➔ mock checkout) functional for MVP review | Quantity updates in cart initially allowed zero and negative numbers | Implemented boundary validation checks in `CartService` before persisting.
* **Week 3 (Aug 10 – Aug 16)**: Standardized `{success, data, error}` JSON envelope and separated DTOs | Password hashes were accidentally exposed in raw user entity responses | Strict separation enforced: `UserResponseDTO` strips `passwordHash` entirely.
* **Week 4 (Aug 17 – Aug 23)**: Multi-role dashboards implemented for Sellers and Admin | Role-based servlet access checks were duplicated across controllers | Centralized all RBAC path protection inside `AuthFilter`.
* **Week 5 (Aug 24 – Aug 30)**: Search with keyword + category filtering and pagination added | Order status workflow lacked database indexing on status column | Created `v2__order_status.sql` migration to index order status for fast retrieval.
* **Week 6 (Aug 31 – Sep 6)**: Product reviews and 5-star ratings with verified purchase check completed | Users without purchase history could submit ratings | Added strict `hasUserPurchasedProduct()` check in `ReviewDAO`.
* **Week 7 (Sep 7 – Sep 13)**: Security hardening complete; all SQL queries confirmed as PreparedStatement | Custom error pages still leaked container version headers | Configured custom error pages in `web.xml` and suppressed stack traces.
* **Week 8 (Sep 14 – Sep 20)**: Full build review and automated CI pipeline established | Docker container volume needed persistence for H2 database file | Configured persistent volume mount for `./data` in Dockerfile.
* **Week 9 (Sep 21 – Sep 27)**: AI Chatbot backend completed using ChatProvider interface with Mock and Gemini providers | Direct external API calls could exceed servlet response timeouts | Implemented 6-second timeout, in-memory caching, and degraded fallback responses.
* **Week 10 (Sep 28 – Oct 4)**: Floating chatbot UI widget integrated with quick FAQ chips | Chat panel overflowed on mobile viewports | Added responsive media queries and auto-scroll to latest message.
* **Week 11 (Oct 5 – Oct 10)**: Final regression clean across all journeys, test suite green, final report and slide deck prepared | Rehearsed demo revealed need for 1-click test credentials | Added quick-fill demo buttons to `login.jsp` for seamless live demonstration.
