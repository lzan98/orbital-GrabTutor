package com.example.grabtutor.Model;

public class RewardsRedemptionHistory {

    String refNo, title, points, status, date;

    public RewardsRedemptionHistory(String refNo, String title, String points, String status, String date) {
        this.refNo = refNo;
        this.title = title;
        this.points = points;
        this.date = date;
        this.status = status;
    }

    public RewardsRedemptionHistory() {
    }

    public String getRefNo() {
        return refNo;
    }

    public String getTitle() {
        return title;
    }

    public String getPoints() {
        return points;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
