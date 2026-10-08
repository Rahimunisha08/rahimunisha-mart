package com.rahimunisha.rahimunishamart.dao;

import com.rahimunisha.rahimunishamart.model.Review;

import java.util.List;

public interface ReviewDAO {
    List<Review> findByProductId(Long productId);
    Review create(Review review);
    boolean hasUserPurchasedProduct(Long userId, Long productId);
    boolean hasUserReviewedProduct(Long userId, Long productId);
    Double getAverageRating(Long productId);
    int getReviewCount(Long productId);
}
