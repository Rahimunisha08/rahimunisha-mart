# RahimunishaMart — Rehearsed Live Demo Script
**Duration**: 3–5 Minutes | **Target Audience**: Evaluation Committee

---

## 1. Preparation Before Demo
1. Ensure the application is started:
   - Check health endpoint: `http://localhost:8080/api/v1/health` (verify returns `{"status":"UP","db":"UP"}`)
2. Open Chrome/Firefox to `http://localhost:8080/home`.

---

## 2. Step-by-Step Demonstration Flow

### Scene 1: Public Storefront & Catalog Discovery (F3) [0:00 – 0:45]
- **Speaker Action**: Show the homepage banner and category filter pills (`Electronics`, `Fashion`, `Home & Living`).
- **Narrative**:
  > *"Welcome to RahimunishaMart. Here on the homepage, customers can explore items across multiple categories. For example, clicking 'Electronics' instantly filters the catalog with pagination and stock indicators."*
- **Live Action**: Type "Headphones" in the search bar and press Enter. Click on **UltraPro ANC Wireless Headphones** to view details.

### Scene 2: Verified Reviews & Stock Inspection (F8) [0:45 – 1:15]
- **Narrative**:
  > *"On the product detail page, we see real-time inventory levels (35 units), high-resolution images, seller attribution, and customer reviews. Notice that review submission is guarded: only buyers who have actually purchased this item can leave a rating."*

### Scene 3: Authentication & Cart Checkout Journey (F1, F4, F5) [1:15 – 2:30]
- **Live Action**: Click **Sign In**. Click the demo **Buyer** button to autofill `nisha@rahimunishamart.com` / `Password@123` and sign in.
- **Narrative**:
  > *"Upon signing in, notice the session ID is securely regenerated to prevent session fixation attacks. The top navbar updates with buyer-specific badges."*
- **Live Action**:
  - Add product to cart.
  - Navigate to Cart: update quantity to 2, observe the running total dynamically recalculate.
  - Click **Proceed to Checkout**.
  - Show the mock payment selection (Mock Card, UPI, COD). Click **Confirm & Place Order**.
- **Narrative**:
  > *"Placing an order executes an atomic database transaction. The order is created, product inventory is safely decremented by 2, and the buyer's cart is cleared in a single atomic commit. If anything fails, the entire transaction rolls back."*
- **Live Action**: Show the order confirmation receipt and the tracking lifecycle timeline (Placed ➔ Confirmed ➔ Shipped ➔ Delivered).

### Scene 4: AI Shopping Assistant (O4) [2:30 – 3:15]
- **Live Action**: Click the floating circular chat launcher (💬) at the bottom-right corner.
- **Narrative**:
  > *"Here is our integrated AI chatbot assistant, developed using the Strategy pattern. It supports both Google Gemini and offline domain rules."*
- **Live Action**: Click the quick chip **"How do I track my order?"** and observe the instant response. Type **"What is the return policy?"** and hit Enter. Show the response.
- **Narrative**:
  > *"The assistant is strictly scoped to e-commerce inquiries and includes per-session sliding-window rate limiting (max 10 queries/minute) and in-memory question caching."*

### Scene 5: Seller & Admin Dashboards (F2, F6, F7, O2, O3) [3:15 – 4:30]
- **Live Action**: Log out and sign in using the demo **Seller** preset (`techseller@rahimunishamart.com`).
- **Narrative**:
  > *"In the Seller Command Center, the merchant sees real-time revenue analytics, units sold, and low-stock alerts. In the Orders section, the seller can advance the order status from 'Confirmed' to 'Shipped'."*
- **Live Action**: Update order status to 'Shipped'. Log out and switch to **Admin** (`admin@rahimunishamart.com`).
- **Narrative**:
  > *"Finally, the Admin portal provides platform-wide governance: managing users, inspecting all orders, and moderating/blocking flagged listings."*

### Scene 6: Conclusion [4:30 – 4:45]
- **Narrative**:
  > *"This concludes the live demonstration of RahimunishaMart. All standing rules, security checklists, and functional requirements F1–F8 plus O1–O4 have been met. I welcome any questions from the committee."*
