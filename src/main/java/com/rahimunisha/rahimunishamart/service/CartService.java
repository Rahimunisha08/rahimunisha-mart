package com.rahimunisha.rahimunishamart.service;

import com.rahimunisha.rahimunishamart.dao.CartDAO;
import com.rahimunisha.rahimunishamart.dao.ProductDAO;
import com.rahimunisha.rahimunishamart.dao.impl.CartDAOImpl;
import com.rahimunisha.rahimunishamart.dao.impl.ProductDAOImpl;
import com.rahimunisha.rahimunishamart.exception.ResourceNotFoundException;
import com.rahimunisha.rahimunishamart.exception.ValidationException;
import com.rahimunisha.rahimunishamart.model.CartItem;
import com.rahimunisha.rahimunishamart.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class CartService {
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartService() {
        this.cartDAO = new CartDAOImpl();
        this.productDAO = new ProductDAOImpl();
    }

    public CartService(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    public List<CartItem> getCart(Long userId) {
        if (userId == null || userId <= 0) {
            throw new ValidationException("userId", "Invalid user ID");
        }
        return cartDAO.findByUserId(userId);
    }

    public BigDecimal getCartTotal(Long userId) {
        List<CartItem> items = getCart(userId);
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem item : items) {
            total = total.add(item.getItemTotal());
        }
        return total;
    }

    public void addToCart(Long userId, Long productId, int quantity) {
        if (quantity <= 0) {
            throw new ValidationException("quantity", "Quantity must be at least 1");
        }
        Product product = productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        if (!"ACTIVE".equalsIgnoreCase(product.getStatus())) {
            throw new ValidationException("status", "This product is currently inactive and cannot be added to cart.");
        }

        if (product.getStockQty() < quantity) {
            throw new ValidationException("stock", "Requested quantity (" + quantity + ") exceeds available stock (" + product.getStockQty() + ")");
        }

        cartDAO.addToCart(userId, productId, quantity);
    }

    public void updateQuantity(Long cartItemId, Long userId, int quantity) {
        if (quantity <= 0) {
            cartDAO.removeFromCart(cartItemId, userId);
            return;
        }

        List<CartItem> items = cartDAO.findByUserId(userId);
        CartItem target = items.stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + cartItemId));

        if (target.getProduct() != null && target.getProduct().getStockQty() < quantity) {
            throw new ValidationException("stock", "Requested quantity exceeds available stock (" + target.getProduct().getStockQty() + ")");
        }

        cartDAO.updateQuantity(cartItemId, userId, quantity);
    }

    public void removeFromCart(Long cartItemId, Long userId) {
        cartDAO.removeFromCart(cartItemId, userId);
    }

    public void clearCart(Long userId) {
        cartDAO.clearCart(userId);
    }

    public int getCartCount(Long userId) {
        return cartDAO.getCartCount(userId);
    }
}
