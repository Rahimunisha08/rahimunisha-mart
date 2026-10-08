package com.rahimunisha.rahimunishamart.dao.impl;

import com.rahimunisha.rahimunishamart.dao.WishlistDAO;
import com.rahimunisha.rahimunishamart.model.Product;
import com.rahimunisha.rahimunishamart.model.WishlistItem;
import com.rahimunisha.rahimunishamart.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAOImpl implements WishlistDAO {

    @Override
    public List<WishlistItem> findByUserId(Long userId) {
        String sql = "SELECT w.id, w.user_id, w.product_id, w.created_at, " +
                     "p.name, p.description, p.category, p.price, p.stock_qty, p.image_url, p.status, p.seller_id " +
                     "FROM wishlist_items w " +
                     "JOIN products p ON w.product_id = p.id " +
                     "WHERE w.user_id = ? ORDER BY w.id DESC";

        List<WishlistItem> list = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    WishlistItem item = new WishlistItem();
                    item.setId(rs.getLong("id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setCreatedAt(rs.getTimestamp("created_at"));

                    Product p = new Product();
                    p.setId(rs.getLong("product_id"));
                    p.setSellerId(rs.getLong("seller_id"));
                    p.setName(rs.getString("name"));
                    p.setDescription(rs.getString("description"));
                    p.setCategory(rs.getString("category"));
                    p.setPrice(rs.getBigDecimal("price"));
                    p.setStockQty(rs.getInt("stock_qty"));
                    p.setImageUrl(rs.getString("image_url"));
                    p.setStatus(rs.getString("status"));
                    item.setProduct(p);

                    list.add(item);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying wishlist", e);
        }
        return list;
    }

    @Override
    public boolean add(Long userId, Long productId) {
        if (exists(userId, productId)) {
            return true;
        }
        String sql = "INSERT INTO wishlist_items (user_id, product_id, created_at) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error adding to wishlist", e);
        }
    }

    @Override
    public boolean remove(Long userId, Long productId) {
        String sql = "DELETE FROM wishlist_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error removing from wishlist", e);
        }
    }

    @Override
    public boolean exists(Long userId, Long productId) {
        String sql = "SELECT COUNT(*) FROM wishlist_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking wishlist", e);
        }
        return false;
    }
}
