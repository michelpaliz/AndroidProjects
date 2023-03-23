package com.example.caminoalba.models;

import java.util.List;

public class Blog {

    private int blog_id;
    private String description;
    private boolean enableInfo;
    private double kmlRunned;
    private int points;
    private List<Blog> followers;
    private List<Blog> following;

    public Blog() {
    }

    public Blog(int blog_id, String description, boolean enableInfo, double kmlRunned, int points) {
        this.blog_id = blog_id;
        this.description = description;
        this.enableInfo = enableInfo;
        this.kmlRunned = kmlRunned;
        this.points = points;
    }

    public int getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(int blog_id) {
        this.blog_id = blog_id;
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

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + blog_id +
                ", description='" + description + '\'' +
                ", enableInfo=" + enableInfo +
                ", kmlRunned=" + kmlRunned +
                ", points=" + points +
                ", followers=" + followers +
                ", following=" + following +
                '}';
    }
}
