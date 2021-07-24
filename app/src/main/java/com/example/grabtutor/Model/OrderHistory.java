package com.example.grabtutor.Model;

public class OrderHistory {

    String refNumber, status, userId, date, postId;
    int postPrice;

    public OrderHistory() {
    }

    public OrderHistory(String refNumber, int postPrice, String status, String postId, String userId, String date) {
        this.refNumber = refNumber;
        this.postPrice = postPrice;
        this.status = status;
        this.userId = userId;
        this.postId = postId;
        this.date = date;
    }


    public String getRefNumber() {
        return refNumber;
    }

    public int getPostPrice() {
        return postPrice;
    }

    public String getStatus() {
        return status;
    }

    public String getPostId() {return postId; }

    public String getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }
}
