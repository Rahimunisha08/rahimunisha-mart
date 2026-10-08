package com.rahimunisha.rahimunishamart.dao;

import com.rahimunisha.rahimunishamart.model.CartItem;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface CartDAO {
    List<CartItem> findByUserId(Long userId);
    Optional<CartItem> findByUserAndProduct(Long userId, Long productId);
    boolean addToCart(Long userId, Long productId, int quantity);
    boolean updateQuantity(Long cartItemId, Long userId, int quantity);
    boolean removeFromCart(Long cartItemId, Long userId);
    boolean clearCart(Long userId);
    boolean clearCart(Long userId, Connection conn);
    int getCartCount(Long userId);
}
