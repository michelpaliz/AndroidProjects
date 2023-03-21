package com.example.caminoalba.models;

import java.util.List;

public class Blog {

    private int id;
    private String description;
    private boolean enableInfo;
    private double kmlRunned;
    private int points;
    private List<Blog> followers;
    private List<Blog> following;


    public Blog(int id, String description, boolean enableInfo, double kmlRunned, int points) {
        this.id = id;
        this.description = description;
        this.enableInfo = enableInfo;
        this.kmlRunned = kmlRunned;
        this.points = points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Blog> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Blog> followers) {
        this.followers = followers;
    }

    public List<Blog> getFollowing() {
        return following;
    }

    public void setFollowing(List<Blog> following) {
        this.following = following;
    }

    public boolean isEnableInfo() {
        return enableInfo;
    }

    public void setEnableInfo(boolean enableInfo) {
        this.enableInfo = enableInfo;
    }

    public double getKmlRunned() {
        return kmlRunned;
    }

    public void setKmlRunned(double kmlRunned) {
        this.kmlRunned = kmlRunned;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
