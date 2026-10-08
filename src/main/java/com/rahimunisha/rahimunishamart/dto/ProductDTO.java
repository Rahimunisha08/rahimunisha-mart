package com.rahimunisha.rahimunishamart.dto;

import com.rahimunisha.rahimunishamart.model.Product;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class ProductDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long sellerId;
    private String sellerName;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private Integer stockQty;
    private String imageUrl;
    private String status;
    private Double averageRating;
    private Integer reviewCount;
    private Timestamp createdAt;

    public ProductDTO() {
    }

    public static ProductDTO fromProduct(Product p) {
        if (p == null) {
            return null;
        }
        ProductDTO dto = new ProductDTO();
        dto.setId(p.getId());
        dto.setSellerId(p.getSellerId());
        dto.setSellerName(p.getSellerName());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setCategory(p.getCategory());
        dto.setPrice(p.getPrice());
        dto.setStockQty(p.getStockQty());
        dto.setImageUrl(p.getImageUrl());
        dto.setStatus(p.getStatus());
        dto.setAverageRating(p.getAverageRating());
        dto.setReviewCount(p.getReviewCount());
        dto.setCreatedAt(p.getCreatedAt());
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSellerId() {
        return sellerId;
    }

    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQty() {
        return stockQty;
    }

    public void setStockQty(Integer stockQty) {
        this.stockQty = stockQty;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
