package com.rahimunisha.rahimunishamart.service;

import com.rahimunisha.rahimunishamart.dao.ProductDAO;
import com.rahimunisha.rahimunishamart.dao.impl.ProductDAOImpl;
import com.rahimunisha.rahimunishamart.exception.ResourceNotFoundException;
import com.rahimunisha.rahimunishamart.exception.UnauthorizedException;
import com.rahimunisha.rahimunishamart.exception.ValidationException;
import com.rahimunisha.rahimunishamart.model.Product;
import com.rahimunisha.rahimunishamart.util.ValidationUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductService {
    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAOImpl();
    }

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public Product createProduct(Long sellerId, String name, String description, String category,
                                 BigDecimal price, Integer stockQty, String imageUrl) {
        validateProductInput(name, category, price, stockQty);

        Product product = new Product();
        product.setSellerId(sellerId);
        product.setName(name.trim());
        product.setDescription(description != null ? description.trim() : "");
        product.setCategory(category.trim());
        product.setPrice(price);
        product.setStockQty(stockQty);
        product.setImageUrl(ValidationUtil.isNonEmpty(imageUrl) ? imageUrl.trim() : "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500");
        product.setStatus("ACTIVE");

        return productDAO.create(product);
    }

    public Product updateProduct(Long sellerId, Long productId, String name, String description,
                                 String category, BigDecimal price, Integer stockQty,
                                 String imageUrl, String status) {
        validateProductInput(name, category, price, stockQty);

        Product existing = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (!existing.getSellerId().equals(sellerId)) {
            throw new UnauthorizedException("You are not authorized to modify this product listing.");
        }

        existing.setName(name.trim());
        existing.setDescription(description != null ? description.trim() : "");
        existing.setCategory(category.trim());
        existing.setPrice(price);
        existing.setStockQty(stockQty);
        if (ValidationUtil.isNonEmpty(imageUrl)) {
            existing.setImageUrl(imageUrl.trim());
        }
        if (ValidationUtil.isNonEmpty(status)) {
            existing.setStatus(status.trim().toUpperCase());
        }

        productDAO.update(existing);
        return existing;
    }

    public boolean deleteProduct(Long sellerId, Long productId) {
        Product existing = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (!existing.getSellerId().equals(sellerId)) {
            throw new UnauthorizedException("You are not authorized to delete this product listing.");
        }

        return productDAO.delete(productId);
    }

    public Product getProductById(Long id) {
        if (id == null || id <= 0) {
            throw new ValidationException("id", "Invalid product ID");
        }
        return productDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    public List<Product> getProducts(String keyword, String category, int page, int pageSize) {
        if (page < 1) page = 1;
        if (pageSize < 1 || pageSize > 100) pageSize = 12;
        int offset = (page - 1) * pageSize;
        return productDAO.findAllActive(keyword, category, offset, pageSize);
    }

    public int countProducts(String keyword, String category) {
        return productDAO.countActive(keyword, category);
    }

    public List<Product> getSellerProducts(Long sellerId) {
        if (sellerId == null || sellerId <= 0) {
            throw new ValidationException("sellerId", "Invalid seller ID");
        }
        return productDAO.findBySellerId(sellerId);
    }

    public List<Product> getAllProductsForAdmin() {
        return productDAO.findAllForAdmin();
    }

    public boolean updateProductStatusByAdmin(Long productId, String status) {
        if (productId == null || productId <= 0) {
            throw new ValidationException("productId", "Invalid product ID");
        }
        return productDAO.updateStatus(productId, status);
    }

    public List<String> getCategories() {
        return productDAO.findAllCategories();
    }

    private void validateProductInput(String name, String category, BigDecimal price, Integer stockQty) {
        Map<String, String> errors = new HashMap<>();

        if (!ValidationUtil.isNonEmpty(name)) {
            errors.put("name", "Product name is required");
        }
        if (!ValidationUtil.isNonEmpty(category)) {
            errors.put("category", "Product category is required");
        }
        if (price == null || !ValidationUtil.isPositive(price)) {
            errors.put("price", "Product price must be a positive number greater than 0");
        }
        if (stockQty == null || !ValidationUtil.isNonNegative(stockQty)) {
            errors.put("stockQty", "Stock quantity must be a non-negative integer");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Validation failed for product", errors);
        }
    }
}
