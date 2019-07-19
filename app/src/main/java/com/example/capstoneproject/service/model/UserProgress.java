package com.example.capstoneproject.service.model;

// Created by akshata on 17/7/19.
public class UserProgress {

    String userId,weight,date;

    public UserProgress(String userId, String weight, String date) {
        this.userId = userId;
        this.weight = weight;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
