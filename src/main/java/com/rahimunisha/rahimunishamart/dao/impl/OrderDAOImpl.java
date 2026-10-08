package com.rahimunisha.rahimunishamart.dao.impl;

import com.rahimunisha.rahimunishamart.dao.OrderDAO;
import com.rahimunisha.rahimunishamart.model.Order;
import com.rahimunisha.rahimunishamart.model.OrderItem;
import com.rahimunisha.rahimunishamart.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderDAOImpl implements OrderDAO {

    @Override
    public Order createOrderWithItems(Order order, List<OrderItem> items) {
        String insertOrderSql = "INSERT INTO orders (buyer_id, total_amount, status, shipping_address, payment_method, payment_status, created_at) " +
                                "VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        String insertItemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price, created_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        String updateStockSql = "UPDATE products SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?";
        String clearCartSql = "DELETE FROM cart_items WHERE user_id = ?";

        Connection conn = null;
        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false); // Begin ACID transaction

            // 1. Insert Order
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setLong(1, order.getBuyerId());
                orderStmt.setBigDecimal(2, order.getTotalAmount());
                orderStmt.setString(3, order.getStatus() != null ? order.getStatus() : "PENDING");
                orderStmt.setString(4, order.getShippingAddress());
                orderStmt.setString(5, order.getPaymentMethod() != null ? order.getPaymentMethod() : "MOCK_CARD");
                orderStmt.setString(6, order.getPaymentStatus() != null ? order.getPaymentStatus() : "PAID");

                int affected = orderStmt.executeUpdate();
                if (affected == 0) {
                    throw new SQLException("Creating order failed, no rows affected.");
                }

                try (ResultSet generatedKeys = orderStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        order.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Creating order failed, no ID obtained.");
                    }
                }
            }

            // 2. Insert Order Items & Update Product Stock
            try (PreparedStatement itemStmt = conn.prepareStatement(insertItemSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement stockStmt = conn.prepareStatement(updateStockSql)) {

                for (OrderItem item : items) {
                    // Update Stock
                    stockStmt.setInt(1, item.getQuantity());
                    stockStmt.setLong(2, item.getProductId());
                    stockStmt.setInt(3, item.getQuantity());
                    int stockUpdated = stockStmt.executeUpdate();
                    if (stockUpdated == 0) {
                        throw new SQLException("Insufficient stock for product ID: " + item.getProductId());
                    }

                    // Insert Order Item
                    itemStmt.setLong(1, order.getId());
                    itemStmt.setLong(2, item.getProductId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setBigDecimal(4, item.getUnitPrice());
                    itemStmt.executeUpdate();

                    try (ResultSet itemKeys = itemStmt.getGeneratedKeys()) {
                        if (itemKeys.next()) {
                            item.setId(itemKeys.getLong(1));
                        }
                    }
                    item.setOrderId(order.getId());
                }
            }

            // 3. Clear Cart for this user
            try (PreparedStatement clearCartStmt = conn.prepareStatement(clearCartSql)) {
                clearCartStmt.setLong(1, order.getBuyerId());
                clearCartStmt.executeUpdate();
            }

            conn.commit(); // Transaction success
            order.setItems(items);
            return order;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // Rollback transaction on failure
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new RuntimeException("Failed to place order transactionally: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public Optional<Order> findById(Long id) {
        String sql = "SELECT o.id, o.buyer_id, o.total_amount, o.status, o.shipping_address, " +
                     "o.payment_method, o.payment_status, o.created_at, u.full_name AS buyer_name, u.email AS buyer_email " +
                     "FROM orders o " +
                     "JOIN users u ON o.buyer_id = u.id " +
                     "WHERE o.id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(fetchOrderItems(conn, order.getId()));
                    return Optional.of(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding order by id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findByBuyerId(Long buyerId) {
        String sql = "SELECT o.id, o.buyer_id, o.total_amount, o.status, o.shipping_address, " +
                     "o.payment_method, o.payment_status, o.created_at, u.full_name AS buyer_name, u.email AS buyer_email " +
                     "FROM orders o " +
                     "JOIN users u ON o.buyer_id = u.id " +
                     "WHERE o.buyer_id = ? " +
                     "ORDER BY o.id DESC";

        List<Order> orders = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, buyerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(fetchOrderItems(conn, order.getId()));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying orders by buyer", e);
        }
        return orders;
    }

    @Override
    public List<Order> findBySellerId(Long sellerId) {
        String sql = "SELECT DISTINCT o.id, o.buyer_id, o.total_amount, o.status, o.shipping_address, " +
                     "o.payment_method, o.payment_status, o.created_at, u.full_name AS buyer_name, u.email AS buyer_email " +
                     "FROM orders o " +
                     "JOIN users u ON o.buyer_id = u.id " +
                     "JOIN order_items oi ON o.id = oi.order_id " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "WHERE p.seller_id = ? " +
                     "ORDER BY o.id DESC";

        List<Order> orders = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, sellerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(fetchOrderItems(conn, order.getId()));
                    orders.add(order);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying seller orders", e);
        }
        return orders;
    }

    @Override
    public List<Order> findAll() {
        String sql = "SELECT o.id, o.buyer_id, o.total_amount, o.status, o.shipping_address, " +
                     "o.payment_method, o.payment_status, o.created_at, u.full_name AS buyer_name, u.email AS buyer_email " +
                     "FROM orders o " +
                     "JOIN users u ON o.buyer_id = u.id " +
                     "ORDER BY o.id DESC";

        List<Order> orders = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Order order = mapOrder(rs);
                order.setItems(fetchOrderItems(conn, order.getId()));
                orders.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying all orders", e);
        }
        return orders;
    }

    @Override
    public boolean updateStatus(Long orderId, String newStatus) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newStatus);
            stmt.setLong(2, orderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating order status", e);
        }
    }

    @Override
    public int countTotalOrders() {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting orders", e);
        }
        return 0;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setBuyerId(rs.getLong("buyer_id"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setStatus(rs.getString("status"));
        o.setShippingAddress(rs.getString("shipping_address"));
        o.setPaymentMethod(rs.getString("payment_method"));
        o.setPaymentStatus(rs.getString("payment_status"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        o.setBuyerName(rs.getString("buyer_name"));
        o.setBuyerEmail(rs.getString("buyer_email"));
        return o;
    }

    private List<OrderItem> fetchOrderItems(Connection conn, Long orderId) throws SQLException {
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price, oi.created_at, " +
                     "p.name AS product_name, p.image_url, p.category " +
                     "FROM order_items oi " +
                     "JOIN products p ON oi.product_id = p.id " +
                     "WHERE oi.order_id = ? ORDER BY oi.id ASC";

        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    item.setOrderId(rs.getLong("order_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    item.setCreatedAt(rs.getTimestamp("created_at"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductImageUrl(rs.getString("image_url"));
                    item.setProductCategory(rs.getString("category"));
                    items.add(item);
                }
            }
        }
        return items;
    }
}
