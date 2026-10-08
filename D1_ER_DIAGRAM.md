# D1 — Entity-Relationship (ER) Diagram
## RahimunishaMart E-Commerce Platform

The following ER diagram details the relational database schema implemented in RahimunishaMart. All foreign key constraints, indexes, cascade policies, and strict `DECIMAL(10,2)` monetary attributes are formally modeled.

```mermaid
erDiagram
    USERS ||--o{ PRODUCTS : "sells / owns"
    USERS ||--o{ ORDERS : "places as buyer"
    USERS ||--o{ CART_ITEMS : "adds to cart"
    USERS ||--o{ REVIEWS : "writes review"
    USERS ||--o{ WISHLIST_ITEMS : "bookmarks"

    PRODUCTS ||--o{ ORDER_ITEMS : "included in line item"
    PRODUCTS ||--o{ CART_ITEMS : "referenced in cart"
    PRODUCTS ||--o{ REVIEWS : "reviewed"
    PRODUCTS ||--o{ WISHLIST_ITEMS : "saved"

    ORDERS ||--|{ ORDER_ITEMS : "contains"

    USERS {
        bigint id PK
        varchar(150) email UK "UNIQUE"
        varchar(255) password_hash "jBCrypt hashed"
        varchar(100) full_name
        varchar(20) role "BUYER, SELLER, ADMIN"
        varchar(20) phone
        varchar(255) address
        timestamp created_at
    }

    PRODUCTS {
        bigint id PK
        bigint seller_id FK "REFERENCES USERS(id)"
        varchar(200) name
        clob description
        varchar(50) category
        decimal(10,2) price "DECIMAL(10,2) precision"
        int stock_qty "Stock check >= 0"
        varchar(500) image_url
        varchar(20) status "ACTIVE, BLOCKED, INACTIVE"
        timestamp created_at
    }

    ORDERS {
        bigint id PK
        bigint buyer_id FK "REFERENCES USERS(id)"
        decimal(10,2) total_amount "DECIMAL(10,2)"
        varchar(20) status "PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED"
        varchar(255) shipping_address
        varchar(50) payment_method "MOCK_CARD, MOCK_UPI, MOCK_COD"
        varchar(30) payment_status "PAID, PENDING"
        timestamp created_at
    }

    ORDER_ITEMS {
        bigint id PK
        bigint order_id FK "REFERENCES ORDERS(id) ON DELETE CASCADE"
        bigint product_id FK "REFERENCES PRODUCTS(id)"
        int quantity
        decimal(10,2) unit_price "DECIMAL(10,2)"
        timestamp created_at
    }

    CART_ITEMS {
        bigint id PK
        bigint user_id FK "REFERENCES USERS(id) ON DELETE CASCADE"
        bigint product_id FK "REFERENCES PRODUCTS(id)"
        int quantity
        timestamp created_at
    }

    REVIEWS {
        bigint id PK
        bigint product_id FK "REFERENCES PRODUCTS(id) ON DELETE CASCADE"
        bigint user_id FK "REFERENCES USERS(id)"
        int rating "1 to 5 Stars"
        clob comment
        timestamp created_at
    }

    WISHLIST_ITEMS {
        bigint id PK
        bigint user_id FK "REFERENCES USERS(id) ON DELETE CASCADE"
        bigint product_id FK "REFERENCES PRODUCTS(id)"
        timestamp created_at
    }
```

### Relational Schema Constraints & Integrity Rules
1. **Monetary Precision**: All monetary values (`price`, `unit_price`, `total_amount`) strictly utilize `DECIMAL(10,2)` rather than floating-point data types.
2. **Audit Timestamps**: Every table contains a non-null `created_at` timestamp default initialized to `CURRENT_TIMESTAMP`.
3. **Foreign Key Indexes**: Dedicated non-unique indexes (`idx_products_seller_id`, `idx_orders_buyer_id`, `idx_order_items_order_id`, etc.) ensure optimal join performance.
4. **Unique Identity**: `users.email` is strictly enforced with a `UNIQUE` index.
