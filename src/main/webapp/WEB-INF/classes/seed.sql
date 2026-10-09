-- ====================================================================
-- RahimunishaMart Seed Data
-- Passwords below are jBCrypt hashed for: "Password@123"
-- Hash: $2a$10$awPRC3ZKT6A9vc1Hg0w/l.cceaKvWrvYBNsOcmDuV4vTYKptwCyMa
-- ====================================================================

-- 1. Users (Admin, Sellers, Buyers)
INSERT INTO users (id, email, password_hash, full_name, role, phone, address, created_at)
VALUES 
(1, 'admin@rahimunishamart.com', '$2a$10$awPRC3ZKT6A9vc1Hg0w/l.cceaKvWrvYBNsOcmDuV4vTYKptwCyMa', 'System Administrator', 'ADMIN', '+91 9876543210', 'HQ Chennai, Tamil Nadu', CURRENT_TIMESTAMP),
(2, 'techseller@rahimunishamart.com', '$2a$10$awPRC3ZKT6A9vc1Hg0w/l.cceaKvWrvYBNsOcmDuV4vTYKptwCyMa', 'TechNova Electronics', 'SELLER', '+91 9876543211', 'Anna Salai, Chennai', CURRENT_TIMESTAMP),
(3, 'styleseller@rahimunishamart.com', '$2a$10$awPRC3ZKT6A9vc1Hg0w/l.cceaKvWrvYBNsOcmDuV4vTYKptwCyMa', 'Aura Fashion & Lifestyle', 'SELLER', '+91 9876543212', 'T Nagar, Chennai', CURRENT_TIMESTAMP),
(4, 'nisha@rahimunishamart.com', '$2a$10$awPRC3ZKT6A9vc1Hg0w/l.cceaKvWrvYBNsOcmDuV4vTYKptwCyMa', 'Rahimunisha Buyer', 'BUYER', '+91 9876543213', 'Guindy, Chennai', CURRENT_TIMESTAMP),
(5, 'demo.buyer@rahimunishamart.com', '$2a$10$awPRC3ZKT6A9vc1Hg0w/l.cceaKvWrvYBNsOcmDuV4vTYKptwCyMa', 'Karthik Raja', 'BUYER', '+91 9876543214', 'Adyar, Chennai', CURRENT_TIMESTAMP);

-- 2. Products
INSERT INTO products (id, seller_id, name, description, category, price, stock_qty, image_url, status, created_at)
VALUES
(1, 2, 'UltraPro ANC Wireless Headphones', 'Active noise cancelling headphones with 40-hour battery life and Hi-Res audio.', 'Electronics', 4999.00, 35, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500', 'ACTIVE', CURRENT_TIMESTAMP),
(2, 2, 'SmartPulse AMOLED Fitness Watch', 'Waterproof fitness smartwatch with SpO2 monitor, continuous heart rate, and GPS tracking.', 'Electronics', 2799.00, 50, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500', 'ACTIVE', CURRENT_TIMESTAMP),
(3, 2, 'Mechanical RGB Gaming Keyboard', 'Tactile blue switches, per-key RGB backlighting, aircraft-grade aluminum frame.', 'Electronics', 3499.00, 20, 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=500', 'ACTIVE', CURRENT_TIMESTAMP),
(4, 3, 'Classic Pure Cotton Oxford Shirt', 'Tailored fit breathable 100% premium cotton shirt designed for formal & casual wear.', 'Fashion', 1299.00, 45, 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=500', 'ACTIVE', CURRENT_TIMESTAMP),
(5, 3, 'Ergonomic Aero Running Shoes', 'Lightweight responsive cushioning running shoes with breathable mesh knit upper.', 'Fashion', 2499.00, 30, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500', 'ACTIVE', CURRENT_TIMESTAMP),
(6, 3, 'Vintage Italian Leather Backpack', 'Handcrafted genuine leather backpack with padded 15.6-inch laptop compartment.', 'Fashion', 3999.00, 15, 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=500', 'ACTIVE', CURRENT_TIMESTAMP),
(7, 2, 'Ergonomic Memory Foam Lumbar Support', 'High-density orthopedic back cushion for office chairs and car seats.', 'Home & Living', 999.00, 60, 'https://images.unsplash.com/photo-1580481077195-c261e47aa130?w=500', 'ACTIVE', CURRENT_TIMESTAMP),
(8, 3, 'Organic Single-Origin Dark Roast Coffee', 'Freshly ground artisanal arabica beans sourced from Coorg estates. 500g pouch.', 'Groceries', 549.00, 80, 'https://images.unsplash.com/photo-1559056199-641a0ac8b55e?w=500', 'ACTIVE', CURRENT_TIMESTAMP);

-- 3. Seed Orders & Items
INSERT INTO orders (id, buyer_id, total_amount, status, shipping_address, payment_method, payment_status, created_at)
VALUES
(1, 4, 4999.00, 'DELIVERED', 'Guindy, Chennai - 600025', 'MOCK_CARD', 'PAID', CURRENT_TIMESTAMP),
(2, 4, 3499.00, 'SHIPPED', 'Guindy, Chennai - 600025', 'MOCK_UPI', 'PAID', CURRENT_TIMESTAMP),
(3, 5, 2799.00, 'CONFIRMED', 'Adyar, Chennai - 600020', 'MOCK_NETBANKING', 'PAID', CURRENT_TIMESTAMP);

INSERT INTO order_items (id, order_id, product_id, quantity, unit_price, created_at)
VALUES
(1, 1, 1, 1, 4999.00, CURRENT_TIMESTAMP),
(2, 2, 3, 1, 3499.00, CURRENT_TIMESTAMP),
(3, 3, 2, 1, 2799.00, CURRENT_TIMESTAMP);

-- 4. Seed Reviews (F8)
INSERT INTO reviews (id, product_id, user_id, rating, comment, created_at)
VALUES
(1, 1, 4, 5, 'Exceptional sound quality and active noise cancellation! Worth every rupee.', CURRENT_TIMESTAMP),
(2, 2, 5, 4, 'Great battery backup and vivid AMOLED screen. Accurate step counter.', CURRENT_TIMESTAMP),
(3, 3, 4, 5, 'Crisp tactile clicks and solid build. Highly recommended for coding and gaming.', CURRENT_TIMESTAMP);

-- 5. Seed Wishlist (O1)
INSERT INTO wishlist_items (id, user_id, product_id, created_at)
VALUES
(1, 4, 6, CURRENT_TIMESTAMP),
(2, 5, 5, CURRENT_TIMESTAMP);
