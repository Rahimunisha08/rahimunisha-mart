# D3 — Sequence Diagram: Place-Order & Checkout Flow
## RahimunishaMart E-Commerce Platform

The sequence diagram below illustrates the end-to-end atomic checkout and mock payment fulfillment flow, highlighting validation steps, ACID transaction boundaries, inventory stock decrements, and cart clearance.

```mermaid
sequenceDiagram
    autonumber
    actor Buyer as 👤 Buyer
    participant Browser as 🌐 Web Browser
    participant AuthFilter as 🛡️ AuthFilter
    participant CheckoutServlet as ⚡ CheckoutServlet
    participant OrderService as ⚙️ OrderService
    participant CartDAO as 🛒 CartDAO
    participant ProductDAO as 📦 ProductDAO
    participant OrderDAO as 💾 OrderDAO
    participant DB as 🗄️ H2 Database

    Buyer->>Browser: Click "Confirm & Place Order" (Address + Payment Method)
    Browser->>AuthFilter: POST /checkout (Session Cookie)
    AuthFilter->>AuthFilter: Verify session & currentUser existence
    AuthFilter->>CheckoutServlet: Forward authorized request

    CheckoutServlet->>OrderService: checkoutCart(buyerId, address, paymentMethod)

    OrderService->>OrderService: Validate address & inputs (reject if blank)
    OrderService->>CartDAO: findByUserId(buyerId)
    CartDAO->>DB: SELECT * FROM cart_items WHERE user_id = ?
    DB-->>CartDAO: Cart item list
    CartDAO-->>OrderService: List<CartItem>

    alt Cart is empty
        OrderService-->>CheckoutServlet: Throw ValidationException("Cart is empty")
        CheckoutServlet-->>Browser: Re-render checkout page with error banner
    else Cart has items
        loop For each CartItem
            OrderService->>ProductDAO: findById(productId)
            ProductDAO->>DB: SELECT * FROM products WHERE id = ?
            DB-->>ProductDAO: Product details
            ProductDAO-->>OrderService: Product entity
            OrderService->>OrderService: Verify product status == ACTIVE & stock >= requested
        end

        OrderService->>OrderDAO: createOrderWithItems(order, orderItems)
        Note over OrderDAO,DB: Begin Transaction (conn.setAutoCommit(false))
        OrderDAO->>DB: INSERT INTO orders VALUES (...)
        DB-->>OrderDAO: Generated order_id

        loop For each line item
            OrderDAO->>DB: UPDATE products SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?
            OrderDAO->>DB: INSERT INTO order_items VALUES (order_id, product_id, qty, price)
        end

        OrderDAO->>DB: DELETE FROM cart_items WHERE user_id = ?
        Note over OrderDAO,DB: Commit Transaction (conn.commit())
        DB-->>OrderDAO: Transaction committed successfully
        OrderDAO-->>OrderService: Confirmed Order entity
        OrderService-->>CheckoutServlet: Order placed
        CheckoutServlet-->>Browser: Redirect 302 to /orders/{id}?success=true
        Browser-->>Buyer: Display confirmed order receipt & tracking timeline
    end
```

### Transactional Guarantees & Edge Case Resilience
1. **ACID Transaction Isolation**: The insertion of `orders`, batch insertion of `order_items`, atomic decrement of product inventory, and emptying of `cart_items` are wrapped in a single database transaction.
2. **Concurrency Safety**: Stock reduction executes conditional SQL updates (`WHERE stock_qty >= ?`). If concurrent buyers deplete the remaining stock before transaction commit, an immediate SQL rollback triggers, preventing overselling.
3. **Fail-Safe Rollback**: In the event of any network interruption or database constraint failure, `conn.rollback()` executes inside the `catch` block, ensuring zero corrupted records.
