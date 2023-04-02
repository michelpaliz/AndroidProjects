package com.example.caminoalba.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Blog implements Serializable {

    private String blog_id;
    private String description;
    private boolean enableInfo;
    private double kmlRunned;
    private int points;
    private List<Blog> followers;
    private List<Blog> following;
    private List<Publication> publications;
    private Profile profile;


    public Blog() {
    }

    public Blog(String blog_id, String description, boolean enableInfo, double kmlRunned, int points, List<Blog> followers, List<Blog> following, List<Publication> publications, Profile profile) {
        this.blog_id = blog_id;
        this.description = description;
        this.enableInfo = enableInfo;
        this.kmlRunned = kmlRunned;
        this.points = points;
        this.followers = followers;
        this.following = following;
        this.publications = publications;
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Publication> getPublications() {
        return publications;
    }

    public void setPublications(List<Publication> publications) {
        this.publications = publications;
    }

    public String getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(String blog_id) {
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

    public void addPublication(Publication publication) {
        // First, we check if the blog object already has a list of publications
        List<Publication> publications = this.getPublications();
        if (publications == null) {
            // If the blog object does not have a list of publications, we create a new one
            publications = new ArrayList<>();
            this.setPublications(publications);
        }
        // Add the new publication to the list of publications for this blog
        publications.add(publication);
    }

    @Override
    public String toString() {
        return "Blog{" +
                "blog_id='" + blog_id + '\'' +
                ", description='" + description + '\'' +
                ", enableInfo=" + enableInfo +
                ", kmlRunned=" + kmlRunned +
                ", points=" + points +
                ", followers=" + followers +
                ", following=" + following +
                ", publications=" + publications +
                ", profile=" + profile +
                '}';
    }
}
