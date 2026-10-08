package com.rahimunisha.rahimunishamart.dao;

import com.rahimunisha.rahimunishamart.model.Product;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface ProductDAO {
    Optional<Product> findById(Long id);
    List<Product> findAllActive(String keyword, String category, int offset, int limit);
    int countActive(String keyword, String category);
    List<Product> findBySellerId(Long sellerId);
    List<Product> findAllForAdmin();
    Product create(Product product);
    boolean update(Product product);
    boolean updateStock(Long productId, int quantityDelta, Connection conn);
    boolean delete(Long id);
    boolean updateStatus(Long id, String status);
    List<String> findAllCategories();
}
