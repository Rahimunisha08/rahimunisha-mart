package com.rahimunisha.rahimunishamart.service;

import com.rahimunisha.rahimunishamart.dao.ProductDAO;
import com.rahimunisha.rahimunishamart.dao.ReviewDAO;
import com.rahimunisha.rahimunishamart.dao.impl.ProductDAOImpl;
import com.rahimunisha.rahimunishamart.dao.impl.ReviewDAOImpl;
import com.rahimunisha.rahimunishamart.exception.ResourceNotFoundException;
import com.rahimunisha.rahimunishamart.exception.ValidationException;
import com.rahimunisha.rahimunishamart.model.Review;
import com.rahimunisha.rahimunishamart.util.ValidationUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewService {
    private final ReviewDAO reviewDAO;
    private final ProductDAO productDAO;

    public ReviewService() {
        this.reviewDAO = new ReviewDAOImpl();
        this.productDAO = new ProductDAOImpl();
    }

    public ReviewService(ReviewDAO reviewDAO, ProductDAO productDAO) {
        this.reviewDAO = reviewDAO;
        this.productDAO = productDAO;
    }

    public Review submitReview(Long userId, Long productId, int rating, String comment) {
        Map<String, String> errors = new HashMap<>();

        if (rating < 1 || rating > 5) {
            errors.put("rating", "Rating must be an integer between 1 and 5 stars");
        }
        if (!ValidationUtil.isNonEmpty(comment)) {
            errors.put("comment", "Review comment cannot be empty");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException("Review validation failed", errors);
        }

        productDAO.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Requirement F8: product reviews and star ratings on completed orders
        if (!reviewDAO.hasUserPurchasedProduct(userId, productId)) {
            throw new ValidationException("purchase", "You can only review products that you have purchased.");
        }

        if (reviewDAO.hasUserReviewedProduct(userId, productId)) {
            throw new ValidationException("duplicate", "You have already submitted a review for this product.");
        }

        Review review = new Review();
        review.setUserId(userId);
        review.setProductId(productId);
        review.setRating(rating);
        review.setComment(ValidationUtil.sanitize(comment));

        return reviewDAO.create(review);
    }

    public List<Review> getProductReviews(Long productId) {
        return reviewDAO.findByProductId(productId);
    }

    public boolean canUserReview(Long userId, Long productId) {
        if (userId == null || productId == null) return false;
        return reviewDAO.hasUserPurchasedProduct(userId, productId) && !reviewDAO.hasUserReviewedProduct(userId, productId);
    }
}
