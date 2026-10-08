package com.rahimunisha.rahimunishamart.service;

import com.rahimunisha.rahimunishamart.dao.WishlistDAO;
import com.rahimunisha.rahimunishamart.dao.impl.WishlistDAOImpl;
import com.rahimunisha.rahimunishamart.model.WishlistItem;

import java.util.List;

public class WishlistService {
    private final WishlistDAO wishlistDAO;

    public WishlistService() {
        this.wishlistDAO = new WishlistDAOImpl();
    }

    public WishlistService(WishlistDAO wishlistDAO) {
        this.wishlistDAO = wishlistDAO;
    }

    public List<WishlistItem> getWishlist(Long userId) {
        return wishlistDAO.findByUserId(userId);
    }

    public boolean addToWishlist(Long userId, Long productId) {
        return wishlistDAO.add(userId, productId);
    }

    public boolean removeFromWishlist(Long userId, Long productId) {
        return wishlistDAO.remove(userId, productId);
    }

    public boolean isInWishlist(Long userId, Long productId) {
        return wishlistDAO.exists(userId, productId);
    }
}
