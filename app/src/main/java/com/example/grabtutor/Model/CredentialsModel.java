package com.example.grabtutor.Model;

public class CredentialsModel {

    String domain, experience, description, refNo, date, status;


    public CredentialsModel(String domain, String experience, String description, String refNo, String date, String status) {
        this.domain = domain;
        this.experience = experience;
        this.description = description;
        this.refNo = refNo;
        this.date = date;
        this.status = status;
    }

    public CredentialsModel() {
    }

    public String getDomain() {
        return domain;
    }

    public String getExperience() {
        return experience;
    }

    public String getDescription() {
        return description;
    }

    public String getRefNo() {
        return refNo;
    }

    public String getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
