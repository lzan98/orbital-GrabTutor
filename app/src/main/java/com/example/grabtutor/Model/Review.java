package com.example.grabtutor.Model;

public class Review {
    private String userId, ratings, review, timestamp;


    public Review(){}

    public Review(String userId, String ratings, String review, String timestamp) {
        this.userId = userId;
        this.ratings = ratings;
        this.review = review;
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRatings() {
        return ratings;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

