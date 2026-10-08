# D2 — Use Case Diagram
## RahimunishaMart E-Commerce Platform

This document describes the functional use cases and actor boundaries spanning all core features (F1–F8) and extensions (O1–O4).

```mermaid
flowchart LR
    subgraph Actors
        Buyer["👤 Buyer"]
        Seller["🏪 Seller"]
        Admin["🛡️ Admin"]
        Guest["🌐 Guest / Visitor"]
    end

    subgraph Authentication & Access ["Authentication & Security (F1)"]
        UC_Register["Register Account (Buyer/Seller)"]
        UC_Login["Sign In (BCrypt Auth & Session Regeneration)"]
        UC_Logout["Log Out (Invalidate Session)"]
    end

    subgraph Storefront & Shopping ["Buyer Operations (F3, F4, F5, F6, F8, O1)"]
        UC_Browse["F3: Browse & Filter by Category / Keyword"]
        UC_Cart["F4: Manage Shopping Cart & Running Total"]
        UC_Checkout["F5: Checkout via Mock Payment Confirmation"]
        UC_OrderHistory["F6: Track Order History & Status"]
        UC_Review["F8: Submit Verified Review & Star Rating"]
        UC_Wishlist["O1: Save Items to Wishlist"]
        UC_Chatbot["O4: Interact with Floating AI Shopping Assistant"]
    end

    subgraph Seller Command ["Seller Operations (F2, F6, O2, O3)"]
        UC_ManageProducts["F2: Create, Edit & Delete Product Listings"]
        UC_StockMonitor["Monitor Real-Time Inventory & Low-Stock Alerts"]
        UC_SellerOrders["F6: View Incoming Line Items & Customer Destinations"]
        UC_OrderStatus["O2: Advance Order Status (Confirmed ➔ Shipped ➔ Delivered)"]
        UC_SalesMetrics["O3: Analyze Sales Revenue & Units Sold"]
    end

    subgraph Administration ["System Governance (F7)"]
        UC_ManageUsers["F7: View & Manage User Directory"]
        UC_Moderate["F7: Moderate & Block Flagged Product Listings"]
        UC_AuditOrders["F7: Global Order Audit Log"]
    end

    Guest --> UC_Register
    Guest --> UC_Login
    Guest --> UC_Browse

    Buyer --> UC_Browse
    Buyer --> UC_Cart
    Buyer --> UC_Checkout
    Buyer --> UC_OrderHistory
    Buyer --> UC_Review
    Buyer --> UC_Wishlist
    Buyer --> UC_Chatbot
    Buyer --> UC_Logout

    Seller --> UC_ManageProducts
    Seller --> UC_StockMonitor
    Seller --> UC_SellerOrders
    Seller --> UC_OrderStatus
    Seller --> UC_SalesMetrics
    Seller --> UC_Logout

    Admin --> UC_ManageUsers
    Admin --> UC_Moderate
    Admin --> UC_AuditOrders
    Admin --> UC_Logout
```

### Traceability to Feature Specifications
- **F1 (Authentication)**: `UC_Register`, `UC_Login`, `UC_Logout` with jBCrypt hashing and session fixation mitigation.
- **F2 (Listing Management)**: `UC_ManageProducts` with image URLs, categorization, and stock counts.
- **F3 (Catalog Discovery)**: `UC_Browse` supporting keyword searches, category filters, and pagination.
- **F4 (Cart Operations)**: `UC_Cart` with real-time running subtotal calculations and quantity boundaries.
- **F5 (Checkout Simulation)**: `UC_Checkout` guaranteeing stock deductions and order creation within atomic DB transactions.
- **F6 (Order Visibility)**: `UC_OrderHistory` for buyers and `UC_SellerOrders` for merchants.
- **F7 (System Administration)**: `UC_ManageUsers`, `UC_Moderate`, and `UC_AuditOrders` with RBAC authorization.
- **F8 (Verified Reviews)**: `UC_Review` enforcing verified purchase checks.
- **O1–O4 (Extensions)**: Wishlist (`UC_Wishlist`), Order status workflow (`UC_OrderStatus`), Sales metrics (`UC_SalesMetrics`), and AI Chatbot Assistant (`UC_Chatbot`).
