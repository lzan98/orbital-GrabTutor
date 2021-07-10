package com.example.grabtutor.Model;

public class Post {

    private String description;
    private String title;
    private String imageurl;
    private String postid;
    private String publisher;
    private String categoryName;
    private String price;
    private int rating;

    public Post() {
    }

    public Post(String title, String description, String imageurl, String categoryName, String price, String postid, String publisher) {
        this.description = description;
        this.imageurl = imageurl;
        this.postid = postid;
        this.title = title;
        this.publisher = publisher;
        this.categoryName = categoryName;
        this.price = price;
        this.rating = 0;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageurl() {
        return imageurl;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPrice() { return price; }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getRating() {
        return rating;
    }
}

