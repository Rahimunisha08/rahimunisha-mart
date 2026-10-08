package com.rahimunisha.rahimunishamart.dao.impl;

import com.rahimunisha.rahimunishamart.dao.ProductDAO;
import com.rahimunisha.rahimunishamart.model.Product;
import com.rahimunisha.rahimunishamart.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {

    @Override
    public Optional<Product> findById(Long id) {
        String sql = "SELECT p.id, p.seller_id, p.name, p.description, p.category, p.price, p.stock_qty, " +
                     "p.image_url, p.status, p.created_at, u.full_name AS seller_name, " +
                     "COALESCE(AVG(r.rating), 0.0) AS avg_rating, COUNT(r.id) AS review_count " +
                     "FROM products p " +
                     "JOIN users u ON p.seller_id = u.id " +
                     "LEFT JOIN reviews r ON p.id = r.product_id " +
                     "WHERE p.id = ? " +
                     "GROUP BY p.id, p.seller_id, p.name, p.description, p.category, p.price, p.stock_qty, " +
                     "p.image_url, p.status, p.created_at, u.full_name";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding product by id", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAllActive(String keyword, String category, int offset, int limit) {
        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.seller_id, p.name, p.description, p.category, p.price, p.stock_qty, " +
                "p.image_url, p.status, p.created_at, u.full_name AS seller_name, " +
                "COALESCE(AVG(r.rating), 0.0) AS avg_rating, COUNT(r.id) AS review_count " +
                "FROM products p " +
                "JOIN users u ON p.seller_id = u.id " +
                "LEFT JOIN reviews r ON p.id = r.product_id " +
                "WHERE p.status = 'ACTIVE' "
        );

        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        boolean hasCategory = (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("ALL"));

        if (hasKeyword) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) ");
        }
        if (hasCategory) {
            sql.append("AND LOWER(p.category) = LOWER(?) ");
        }

        sql.append("GROUP BY p.id, p.seller_id, p.name, p.description, p.category, p.price, p.stock_qty, ")
           .append("p.image_url, p.status, p.created_at, u.full_name ")
           .append("ORDER BY p.id DESC LIMIT ? OFFSET ?");

        List<Product> products = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            if (hasKeyword) {
                String searchPattern = "%" + keyword.trim().toLowerCase() + "%";
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
            }
            if (hasCategory) {
                stmt.setString(paramIndex++, category.trim());
            }
            stmt.setInt(paramIndex++, limit);
            stmt.setInt(paramIndex, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error searching products", e);
        }
        return products;
    }

    @Override
    public int countActive(String keyword, String category) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM products p WHERE p.status = 'ACTIVE' ");
        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        boolean hasCategory = (category != null && !category.trim().isEmpty() && !category.equalsIgnoreCase("ALL"));

        if (hasKeyword) {
            sql.append("AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?) ");
        }
        if (hasCategory) {
            sql.append("AND LOWER(p.category) = LOWER(?) ");
        }

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (hasKeyword) {
                String searchPattern = "%" + keyword.trim().toLowerCase() + "%";
                stmt.setString(paramIndex++, searchPattern);
                stmt.setString(paramIndex++, searchPattern);
            }
            if (hasCategory) {
                stmt.setString(paramIndex, category.trim());
            }
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error counting products", e);
        }
        return 0;
    }

    @Override
    public List<Product> findBySellerId(Long sellerId) {
        String sql = "SELECT p.id, p.seller_id, p.name, p.description, p.category, p.price, p.stock_qty, " +
                     "p.image_url, p.status, p.created_at, u.full_name AS seller_name, " +
                     "COALESCE(AVG(r.rating), 0.0) AS avg_rating, COUNT(r.id) AS review_count " +
                     "FROM products p " +
                     "JOIN users u ON p.seller_id = u.id " +
                     "LEFT JOIN reviews r ON p.id = r.product_id " +
                     "WHERE p.seller_id = ? " +
                     "GROUP BY p.id, p.seller_id, p.name, p.description, p.category, p.price, p.stock_qty, " +
                     "p.image_url, p.status, p.created_at, u.full_name " +
                     "ORDER BY p.id DESC";

        List<Product> products = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, sellerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSet(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying products by seller", e);
        }
        return products;
    }

    @Override
    public List<Product> findAllForAdmin() {
        String sql = "SELECT p.id, p.seller_id, p.name, p.description, p.category, p.price, p.stock_qty, " +
                     "p.image_url, p.status, p.created_at, u.full_name AS seller_name, " +
                     "COALESCE(AVG(r.rating), 0.0) AS avg_rating, COUNT(r.id) AS review_count " +
                     "FROM products p " +
                     "JOIN users u ON p.seller_id = u.id " +
                     "LEFT JOIN reviews r ON p.id = r.product_id " +
                     "GROUP BY p.id, p.seller_id, p.name, p.description, p.category, p.price, p.stock_qty, " +
                     "p.image_url, p.status, p.created_at, u.full_name " +
                     "ORDER BY p.id DESC";

        List<Product> products = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                products.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying all products for admin", e);
        }
        return products;
    }

    @Override
    public Product create(Product product) {
        String sql = "INSERT INTO products (seller_id, name, description, category, price, stock_qty, image_url, status, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, product.getSellerId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setString(4, product.getCategory());
            stmt.setBigDecimal(5, product.getPrice());
            stmt.setInt(6, product.getStockQty() != null ? product.getStockQty() : 0);
            stmt.setString(7, product.getImageUrl());
            stmt.setString(8, product.getStatus() != null ? product.getStatus() : "ACTIVE");

            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        product.setId(rs.getLong(1));
                    }
                }
            }
            return product;
        } catch (SQLException e) {
            throw new RuntimeException("Error inserting product", e);
        }
    }

    @Override
    public boolean update(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, category = ?, price = ?, stock_qty = ?, image_url = ?, status = ? " +
                     "WHERE id = ? AND seller_id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setString(3, product.getCategory());
            stmt.setBigDecimal(4, product.getPrice());
            stmt.setInt(5, product.getStockQty());
            stmt.setString(6, product.getImageUrl());
            stmt.setString(7, product.getStatus());
            stmt.setLong(8, product.getId());
            stmt.setLong(9, product.getSellerId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product", e);
        }
    }

    @Override
    public boolean updateStock(Long productId, int quantityDelta, Connection conn) {
        // quantityDelta is negative when purchasing (e.g. -2)
        String sql = "UPDATE products SET stock_qty = stock_qty + ? WHERE id = ? AND stock_qty + ? >= 0";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantityDelta);
            stmt.setLong(2, productId);
            stmt.setInt(3, quantityDelta);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating stock quantity", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting product", e);
        }
    }

    @Override
    public boolean updateStatus(Long id, String status) {
        String sql = "UPDATE products SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setLong(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating product status", e);
        }
    }

    @Override
    public List<String> findAllCategories() {
        String sql = "SELECT DISTINCT category FROM products WHERE status = 'ACTIVE' ORDER BY category ASC";
        List<String> categories = new ArrayList<>();
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error querying categories", e);
        }
        return categories;
    }

    private Product mapResultSet(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setSellerId(rs.getLong("seller_id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setCategory(rs.getString("category"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQty(rs.getInt("stock_qty"));
        p.setImageUrl(rs.getString("image_url"));
        p.setStatus(rs.getString("status"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setSellerName(rs.getString("seller_name"));
        p.setAverageRating(rs.getDouble("avg_rating"));
        p.setReviewCount(rs.getInt("review_count"));
        return p;
    }
}
