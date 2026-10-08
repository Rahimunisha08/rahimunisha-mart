-- ====================================================================
-- Migration: v2__order_status.sql
-- Goal: Formalize order status transitions: PENDING -> CONFIRMED -> SHIPPED -> DELIVERED (and CANCELLED)
-- ====================================================================

-- Ensure status check constraint or validation column is aligned
-- For H2 / ANSI SQL:
ALTER TABLE orders ALTER COLUMN status SET DEFAULT 'PENDING';

-- Index on order status for fast seller & admin dashboard queries
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders(created_at);
