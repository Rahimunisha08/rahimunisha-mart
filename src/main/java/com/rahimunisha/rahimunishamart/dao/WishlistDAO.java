package com.rahimunisha.rahimunishamart.dao;

import com.rahimunisha.rahimunishamart.model.WishlistItem;

import java.util.List;

public interface WishlistDAO {
    List<WishlistItem> findByUserId(Long userId);
    boolean add(Long userId, Long productId);
    boolean remove(Long userId, Long productId);
    boolean exists(Long userId, Long productId);
}
