package com.example.hairsalon.model;

import java.time.LocalDateTime;

public class Review {
    private Integer reviewId;


    public Review() {

    }
    public Integer getReviewId() {
        return reviewId;
    }

    public void setReviewId(Integer reviewId) {
        this.reviewId = reviewId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    private String customerName;
    private String comment;
    private Integer rating;
    private String reviewDate;

    public Review(Integer reviewId, String customerName, String comment, Integer rating, String reviewDate) {
        this.reviewId = reviewId;
        this.customerName = customerName;
        this.comment = comment;
        this.rating = rating;
        this.reviewDate = reviewDate;
    }
}
