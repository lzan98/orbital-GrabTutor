package com.example.grabtutor.Model;

public class RewardsRedemptionModel {

    String title, points, picture;

    public RewardsRedemptionModel(String title, String points, String picture) {
        this.title = title;
        this.points = points;
        this.picture = picture;
    }

    public RewardsRedemptionModel() {
    }

    public String getTitle() {
        return title;
    }

    public String getPoints() {
        return points;
    }

    public String getPicture() {
        return picture;
    }
}
